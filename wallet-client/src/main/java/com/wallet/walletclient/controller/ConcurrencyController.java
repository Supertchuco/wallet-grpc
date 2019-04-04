package com.wallet.walletclient.controller;

import com.wallet.walletclient.service.ConcurrencyService;
import com.wallet.walletclient.vo.ConcurrencyInputsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concurrency")
public class ConcurrencyController {

    @Autowired
    private ConcurrencyService concurrencyService;

    @PostMapping(path = "/startConcurrencyProcess")
    public HttpStatus startConcurrencyProcess(@RequestBody final ConcurrencyInputsVO concurrencyInputsVO) {
        concurrencyService.startUsersConcurrently(concurrencyInputsVO.getNumberOfUsers(),
                concurrencyInputsVO.getConcurrentThreadsPerUser(), concurrencyInputsVO.getNumberOfRoundsPerThread());
        return HttpStatus.OK;
    }

}
