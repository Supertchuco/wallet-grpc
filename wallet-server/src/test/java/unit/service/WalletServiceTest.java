package unit.service;

import com.wallet.proto.CURRENCY;
import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.entity.Wallet;
import com.wallet.walletserver.enumerator.Operation;
import com.wallet.walletserver.exception.AmountIsZeroException;
import com.wallet.walletserver.exception.InsufficientFundsException;
import com.wallet.walletserver.exception.InvalidArgumentException;
import com.wallet.walletserver.exception.OperationNotRecognizedException;
import com.wallet.walletserver.service.UserService;
import com.wallet.walletserver.service.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserService userService;

    private User user;
    private User user2;

    @Before
    public void setUp() {

        Wallet wallet1 = new Wallet(1, new BigDecimal("100.00"), CURRENCY.EUR);
        Wallet wallet2 = new Wallet(1, new BigDecimal("40.50"), CURRENCY.GBP);
        user = new User(123, "testUser", Arrays.asList(wallet1, wallet2));
        user2 = new User(145, "testUser", null);
    }

    @Test(expected = AmountIsZeroException.class)
    public void checkIfAmountIsZeroWhenInputIsZeroTest() {
        ReflectionTestUtils.invokeMethod(walletService, "checkIfAmountIsZero", new BigDecimal[]{new BigDecimal("0.0")});
    }

    @Test(expected = AmountIsZeroException.class)
    public void checkIfAmountIsZeroWhenInputIsNullTest() {
        ReflectionTestUtils.invokeMethod(walletService, "checkIfAmountIsZero", new BigDecimal[]{null});
    }

    @Test
    public void checkIfAmountIsZeroWhenInputIsNotZeroTest() {
        ReflectionTestUtils.invokeMethod(walletService, "checkIfAmountIsZero", new BigDecimal[]{new BigDecimal("1.0")});
    }

    @Test
    public void checkIfBalanceIsEnoughToWithdrawOperationWhenFundsIsEnoughTest() {
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("1.0"), new BigDecimal("2.0")});
    }

    @Test(expected = InsufficientFundsException.class)
    public void checkIfBalanceIsEnoughToWithdrawOperationWhenFundsNotEnoughTest() {
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("3.0"), new BigDecimal("2.0")});
    }

    public void checkIfBalanceIsEnoughToWithdrawOperationWhenFundsAndWithdrawValuesAreEquals() {
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("3.0"), new BigDecimal("3.0")});
    }

    @Test(expected = InsufficientFundsException.class)
    public void checkIfBalanceIsEnoughToWithdrawOperationWhenFundsIsNull() {
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("3.0"), null});
    }

    @Test
    public void depositOperationHappyScenarioWhenUserHasCurrencyWalletTest() {
        when(userService.userHasCurrencyWallet(Mockito.any(User.class), Mockito.any(CURRENCY.class))).thenReturn(true);
        when(userService.findUserById(Mockito.anyInt())).thenReturn(user);
        walletService.depositOperation(new BigDecimal("10.01"), 234, CURRENCY.EUR);
    }


    @Test
    public void withdrawOperationHappyScenarioTest() {
        when(userService.userHasCurrencyWallet(Mockito.any(User.class), Mockito.any(CURRENCY.class))).thenReturn(true);
        when(userService.findUserById(Mockito.anyInt())).thenReturn(user);
        walletService.withdrawOperation(new BigDecimal("10.01"), 234, CURRENCY.GBP);
    }

    @Test(expected = InvalidArgumentException.class)
    public void withdrawOperationWhenClientNotHaveCurrencyWalletTest() {
        when(userService.userHasCurrencyWallet(Mockito.any(User.class), Mockito.any(CURRENCY.class))).thenReturn(false);
        when(userService.findUserById(Mockito.anyInt())).thenReturn(user);
        walletService.withdrawOperation(new BigDecimal("10.01"), 234, CURRENCY.GBP);
    }

    @Test
    public void updateWalletBalanceValueHappyScenarioWithDrawOperationTest() {
        assertEquals(new BigDecimal("4.50"), ReflectionTestUtils.invokeMethod(walletService, "updateWalletBalanceValue",
                new Object[]{new BigDecimal("10"), new BigDecimal("5.50"), Operation.WITHDRAW.name()}));
    }

    @Test
    public void updateWalletBalanceValueHappyScenarioWithDepositOperationTest() {
        assertEquals(new BigDecimal("10.75"), ReflectionTestUtils.invokeMethod(walletService, "updateWalletBalanceValue",
                new Object[]{new BigDecimal("10"), new BigDecimal("0.75"), Operation.DEPOSIT.name()}));
    }

    @Test(expected = OperationNotRecognizedException.class)
    public void updateWalletBalanceValueWhenOperationIsInvalidTest() {
        ReflectionTestUtils.invokeMethod(walletService, "updateWalletBalanceValue",
                new Object[]{new BigDecimal("10"), new BigDecimal("0.75"), "OtherThing"});
    }

    @Test
    public void getWalletsBalanceHappyScenarioTest() {
        final String GET_BALANCE_RESULT = "{\"walletBalanceList\":[{\"currency\":\"EUR\",\"balance\":\"100.00\"},{\"currency\":\"GBP\",\"balance\":\"40.50\"}]}";
        when(userService.findUserById(Mockito.anyInt())).thenReturn(user);
        assertEquals(GET_BALANCE_RESULT, walletService.getWalletsBalance(123));
    }

    @Test
    public void getWalletsBalanceWhenUserDoesNotHaveWalletTest() {
         when(userService.findUserById(Mockito.anyInt())).thenReturn(user2);
        assertEquals("{\"walletBalanceList\":[]}", walletService.getWalletsBalance(145));
    }
}
