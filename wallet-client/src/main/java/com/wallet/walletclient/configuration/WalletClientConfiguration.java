package com.wallet.walletclient.configuration;

import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wallet.proto.WalletServiceGrpc;
import io.grpc.Channel;

@Configuration
public class WalletClientConfiguration {
    @GrpcClient("wallet-server")
    private Channel serverChannel;

    @Bean
    WalletServiceGrpc.WalletServiceFutureStub getWalletServiceFutureStub() {
        return WalletServiceGrpc.newFutureStub(serverChannel);
    }
}