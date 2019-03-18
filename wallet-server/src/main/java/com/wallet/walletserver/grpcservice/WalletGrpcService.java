package com.wallet.walletserver.grpcservice;


import com.wallet.proto.BaseRequest;
import com.wallet.proto.BaseResponse;
import com.wallet.proto.OPERATION;
import com.wallet.proto.STATUS;
import com.wallet.proto.StatusMessage;
import com.wallet.proto.WalletServiceGrpc;
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

            final BigDecimal depositValue = new BigDecimal(request.getAmount());
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

  /*  @Override
    @Transactional

    public void balance(final BaseRequest request, final StreamObserver<BaseResponse> responseObserver) {
        logger.info("Request Recieved for UserID:{}", request.getUserID());
        try {
            Optional<List<Wallet>> userWallets = walletRepository.findByWalletPK_UserID(request.getUserID());
            bpWalletValidator.validate(userWallets);
            String balance = balanceResponseDTO.getBalanceAsString(userWallets);
            logger.info(balance);
            responseObserver.onNext(BaseResponse.newBuilder().setStatusMessage(balance)
                .setStatus((STATUS.TRANSACTION_SUCCESS)).setOperation(OPERATION.BALANCE).build());
            responseObserver.onCompleted();
        } catch (BPValidationException e) {
            logger.error(e.getErrorStatus().name());
            responseObserver
                .onError(new StatusRuntimeException(e.getStatus().withDescription(e.getErrorStatus().name())));
        } catch (Exception e) {
            logger.error("------------>", e);
            responseObserver.onError(new StatusRuntimeException(Status.UNKNOWN.withDescription(e.getMessage())));
        } finally {

        }

    }*/


    private void successResponse(final StreamObserver<BaseResponse> responseObserver, OPERATION operation) {
        responseObserver.onNext(
            BaseResponse.newBuilder().setStatus(STATUS.TRANSACTION_SUCCESS).setOperation(operation).build());
        responseObserver.onCompleted();
    }




    // deletar https://github.com/Akash-Mittal/bp-wallet-grpc-spring/blob/master/bp-wallet-server/src/main/java/com/bp/wallet/server/validation/BPWalletValidator.java

}
