package com.wallet.walletclient.grpcservice;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.wallet.proto.BaseRequest;
import com.wallet.proto.BaseResponse;
import com.wallet.proto.OPERATION;
import com.wallet.proto.WalletServiceGrpc;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletClientGrpcService {

    public ListenableFuture<BaseResponse> deposit(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                                  final BaseRequest baseRequest, final TaskExecutor taskExecutor) {

        ListenableFuture<BaseResponse> response = futureStub.deposit(baseRequest);
        Futures.addCallback(response, new FutureCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                result.toBuilder().setOperation(OPERATION.DEPOSIT);
                log.info("{} {}", result.getStatus().name(), result.getStatusMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                log.error(Status.fromThrowable(t).getDescription());
            }
        }, taskExecutor);

        return response;

    }

    public ListenableFuture<BaseResponse> withdraw(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                                   final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        ListenableFuture<BaseResponse> response = futureStub.withdraw(baseRequest);

        Futures.addCallback(response, new FutureCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                result.toBuilder().setOperation(OPERATION.WITHDRAW);

                log.info("{} {}", result.getStatus().name(), result.getStatusMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                log.error(Status.fromThrowable(t).getDescription());
            }
        }, taskExecutor);
        return response;

    }

    public ListenableFuture<BaseResponse> balance(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                                  final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        ListenableFuture<BaseResponse> response = futureStub.balance(baseRequest);

        Futures.addCallback(response, new FutureCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                result.toBuilder().setOperation(OPERATION.BALANCE);
                log.info("{} {}", result.getStatus().name(), result.getStatusMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                log.error(Status.fromThrowable(t).getDescription());
            }
        }, taskExecutor);
        return response;

    }
}
