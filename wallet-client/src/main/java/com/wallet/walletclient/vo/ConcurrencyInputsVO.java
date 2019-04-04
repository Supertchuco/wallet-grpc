package com.wallet.walletclient.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConcurrencyInputsVO implements Serializable {

    private int numberOfUsers;
    private int concurrentThreadsPerUser;
    private int numberOfRoundsPerThread;
}
