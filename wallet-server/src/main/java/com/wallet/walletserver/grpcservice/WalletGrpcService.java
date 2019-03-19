package com.wallet.walletserver.grpcservice;


import com.wallet.proto.*;
import com.wallet.walletserver.exception.AmountIsZeroException;
import com.wallet.walletserver.exception.InsufficientFundsException;
import com.wallet.walletserver.exception.InvalidArgumentException;
import com.wallet.walletserver.exception.UserNotFoundException;
import com.wallet.walletserver.service.UserService;
import com.wallet.walletserver.service.WalletService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Slf4j
@GRpcService
public class WalletGrpcService extends WalletServiceGrpc.WalletServiceImplBase {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;


    @Override
    @Transactional
    public void deposit(final BaseRequest request, final StreamObserver<BaseResponse> responseObserver) {
        try {

            log.info("Deposit operation user id:{} For Amount:{} and Currency: {}", request.getUserID(), request.getAmount(),
                    request.getCurrency());

            if (StringUtils.isEmpty(request.getAmount())) {
                log.error("Some or more arguments is (are) invalid(s)");
                throw new InvalidArgumentException("Some or more arguments is (are) invalid(s)");
            }

            walletService.depositOperation(new BigDecimal(request.getAmount()), request.getUserID());
            successResponse(responseObserver, OPERATION.DEPOSIT);
            log.info("Operation finished with success");

        } catch (InvalidArgumentException | NumberFormatException invalidArgumentException) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.INVALID_ARGUMENTS.toString())));

        } catch (AmountIsZeroException amountIsZeroException) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.toString())));

        } catch (UserNotFoundException userNotFoundException) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.toString())));

        } catch (Exception e) {
            log.error("Error on deposit operation", e);
            responseObserver.onError(new StatusRuntimeException(Status.UNKNOWN.withDescription(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public void withdraw(final BaseRequest request, final StreamObserver<BaseResponse> responseObserver) {
        try {

            log.info("Withdraw operation user id:{} For withdraw value:{} and Currency: {}", request.getUserID(), request.getAmount(),
                    request.getCurrency());

            if (StringUtils.isEmpty(request.getAmount())) {
                log.error("Some or more arguments is (are) invalid(s)");
                throw new InvalidArgumentException("Some or more arguments is (are) invalid(s)");
            }

            final BigDecimal withdrawValue = new BigDecimal(request.getAmount());
            walletService.withdrawOperation(new BigDecimal(request.getAmount()), request.getUserID());
            successResponse(responseObserver, OPERATION.WITHDRAW);
            log.info("Operation finished with success");

        } catch (InvalidArgumentException | NumberFormatException exception) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.INVALID_ARGUMENTS.name())));

        } catch (AmountIsZeroException amountIsZeroException) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));

        } catch (InsufficientFundsException insufficientFundsException) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.INSUFFICIENT_BALANCE.name())));

        } catch (UserNotFoundException userNotFoundException) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.name())));

        } catch (Exception e) {
            log.error("Error on deposit operation", e);
            responseObserver.onError(new StatusRuntimeException(Status.UNKNOWN.withDescription(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public void balance(final BaseRequest request, final StreamObserver<BaseResponse> responseObserver) {
        try {

            log.info("Get balance operation user id:{} For withdraw value:{} and Currency: {}", request.getUserID(), request.getAmount(),
                    request.getCurrency());

            String balance = walletService.getBalance(request.getUserID()).toString();
            log.info(balance);
            responseObserver.onNext(BaseResponse.newBuilder().setStatusMessage(balance)
                    .setStatus((STATUS.TRANSACTION_SUCCESS)).setOperation(OPERATION.BALANCE).build());
            responseObserver.onCompleted();
        } catch (UserNotFoundException userNotFoundException) {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.name())));

        } catch (Exception e) {
            log.error("Error on deposit operation", e);
            responseObserver.onError(new StatusRuntimeException(Status.UNKNOWN.withDescription(e.getMessage())));
        }

    }


    private void successResponse(final StreamObserver<BaseResponse> responseObserver, OPERATION operation) {
        responseObserver.onNext(
                BaseResponse.newBuilder().setStatus(STATUS.TRANSACTION_SUCCESS).setOperation(operation).build());
        responseObserver.onCompleted();
    }


}
