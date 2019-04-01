package com.wallet.walletclient.endtoend;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest {

    private static final String requestEndpointBase = "http://localhost:8081/wallet-client/wallet";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static String readJSON(String filename) {
        try {
            return FileUtils.readFileToString(ResourceUtils.getFile("classpath:" + filename), "UTF-8");
        } catch (IOException exception) {
            return null;
        }
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @Sql("/sql/seed.sql")
    @Sql(scripts = "/sql/purge.sql", executionPhase = AFTER_TEST_METHOD)
    public void integrationTestOrchestrator() {
        shouldReturnInsufficientFoundsWhenUserNotHave200USDForWithdrawOperation();
        shouldReturn200WhenMakeDepositOperationWith100USD();
        shouldReturn200WhenMakeBalanceOperation("{\"currency\":\"USD\",\"balance\":\"100.00\"}");
        shouldReturnInsufficientFoundsWhenUserNotHave200USDForWithdrawOperation();
        shouldReturn200WhenMakeDepositOperationWith100EUR();
        shouldReturn200WhenMakeBalanceOperation("{\"walletBalanceList\":[{\"currency\":\"USD\",\"balance\":\"100.00\"},{\"currency\":\"EUR\",\"balance\":\"100.00\"}]}");
        shouldReturnInsufficientFoundsWhenUserNotHave200USDForWithdrawOperation();
        shouldReturn200WhenMakeDepositOperationWith100USD();
        shouldReturn200WhenMakeBalanceOperation("{\"walletBalanceList\":[{\"currency\":\"USD\",\"balance\":\"200.00\"},{\"currency\":\"EUR\",\"balance\":\"100.00\"}]}");
        shouldReturn200WhenUserNotHave200USDForWithdrawOperation();
        shouldReturn200WhenMakeBalanceOperation("{\"walletBalanceList\":[{\"currency\":\"USD\",\"balance\":\"0.00\"},{\"currency\":\"EUR\",\"balance\":\"100.00\"}]}");
        shouldReturnInsufficientFoundsWhenUserNotHave200USDForWithdrawOperation();
    }


    public void shouldReturnInsufficientFoundsWhenUserNotHave200USDForWithdrawOperation() {
        String payload = readJSON("request/insufficientFoundsWithdraw.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/withdraw"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Insufficient founds"));
    }


    public void shouldReturn200WhenMakeDepositOperationWith100USD() {
        String payload = readJSON("request/success100USDDeposit.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/deposit"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    public void shouldReturn200WhenMakeBalanceOperation(final String responseBody) {
        HttpEntity<String> entity = new HttpEntity<String>(buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/balance?userId=1"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(responseBody));
    }

    public void shouldReturn200WhenMakeDepositOperationWith100EUR() {
        String payload = readJSON("request/success100EURDeposit.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/deposit"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    public void shouldReturn200WhenUserNotHave200USDForWithdrawOperation() {
        String payload = readJSON("request/insufficientFoundsWithdraw.json");
        HttpEntity<String> entity = new HttpEntity<String>(payload, buildHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpointBase.concat("/withdraw"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
