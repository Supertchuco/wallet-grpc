package com.wallet.walletserver.service;


import com.wallet.proto.BaseRequest;
import com.wallet.proto.BaseResponse;
import com.wallet.proto.OPERATION;
import com.wallet.proto.STATUS;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.repository.WalletRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Slf4j
@GRpcService
public class WalletService extends WalletServiceGrpc.WalletServiceImplBase {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;


    @Override
    @Transactional
    public void deposit(final BaseRequest request, final StreamObserver<BaseResponse> responseObserver) {
        try {
            validateRequest(request);
            final BigDecimal balanceToADD = new BigDecimal(request.getAmount());
            log.info("Deposit operation user id:{} For Amount:{} and Currency: {}", request.getUserID(), request.getAmount(),
                request.getCurrency());

            User user = userService.findUserById(request.getUserID());
            userService.validateUser(user);

        /*    Optional<Wallet> wallet = getUserWallet(request);
            bpWalletValidator.validateWallet(wallet);
            updateWallet(wallet.get().getBalance().add(balanceToADD), wallet);*/


            successResponse(responseObserver, OPERATION.DEPOSIT);
            log.info("Operation finished with success");

        } catch (Exception e) {
            log.error("Error on deposit operation", e);
            responseObserver.onError(new StatusRuntimeException(Status.UNKNOWN.withDescription(e.getMessage())));
        }
    }

   /* @Override
    @Transactional
    public void withdraw(final BaseRequest request, final StreamObserver<BaseResponse> responseObserver) {

        logger.info("Request Recieved for UserID:{} For Amount:{}{} ", request.getUserID(), request.getAmount(),
            request.getCurrency());
        try {
            final BigDecimal balanceToWithdraw = get(request.getAmount());
            validateRequest(request);
            Optional<Wallet> wallet = getUserWallet(request);
            validateWithDrawRequest(balanceToWithdraw, wallet);
            updateWallet(wallet.get().getBalance().subtract(balanceToWithdraw), wallet);
            successResponse(responseObserver, OPERATION.WITHDRAW);
        } catch (BPValidationException e) {
            logger.error(e.getErrorStatus().name());
            responseObserver
                .onError(new StatusRuntimeException(e.getStatus().withDescription(e.getErrorStatus().name())));
        } catch (Exception e) {
            logger.error("------------>", e);
            responseObserver.onError(new StatusRuntimeException(Status.UNKNOWN.withDescription(e.getMessage())));
        } finally {
            walletRepository.flush();
        }
    }

    @Override
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


    public void validateRequest(final BaseRequest request) {


    }


    private void successResponse(final StreamObserver<BaseResponse> responseObserver, OPERATION operation) {
        responseObserver.onNext(
            BaseResponse.newBuilder().setStatus(STATUS.TRANSACTION_SUCCESS).setOperation(operation).build());
        responseObserver.onCompleted();
    }


}
