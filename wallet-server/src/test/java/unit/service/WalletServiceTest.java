package unit.service;

import com.wallet.walletserver.exception.AmountIsZeroException;
import com.wallet.walletserver.exception.InsufficientFundsException;
import com.wallet.walletserver.service.UserService;
import com.wallet.walletserver.service.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {


    }

    @Test(expected = AmountIsZeroException.class)
    public void checkIfAmountIsZeroWhenInputIsZeroTest(){
        ReflectionTestUtils.invokeMethod(walletService, "checkIfAmountIsZero", new BigDecimal[]{new BigDecimal("0.0")} );
    }

    @Test(expected = AmountIsZeroException.class)
    public void checkIfAmountIsZeroWhenInputIsNullTest(){
        ReflectionTestUtils.invokeMethod(walletService, "checkIfAmountIsZero", new BigDecimal[]{null} );
    }

    @Test
    public void checkIfAmountIsZeroWhenInputIsNotZeroTest(){
        ReflectionTestUtils.invokeMethod(walletService, "checkIfAmountIsZero", new BigDecimal[]{new BigDecimal("1.0")} );
    }

    @Test
    public void  checkIfBalanceIsEnoughToWithdrawOperationWhenFundsIsEnoughTest(){
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("1.0"), new BigDecimal("2.0")} );
    }

    @Test(expected = InsufficientFundsException.class)
    public void  checkIfBalanceIsEnoughToWithdrawOperationWhenFundsNotEnoughTest(){
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("3.0"), new BigDecimal("2.0")} );
    }

    public void  checkIfBalanceIsEnoughToWithdrawOperationWhenFundsAndWithdrawValuesAreEquals(){
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("3.0"), new BigDecimal("3.0")} );
    }

    @Test(expected = InsufficientFundsException.class)
    public void  checkIfBalanceIsEnoughToWithdrawOperationWhenFundsIsNull(){
        ReflectionTestUtils.invokeMethod(walletService, "checkIfBalanceIsEnoughToWithdrawOperation", new BigDecimal[]{new BigDecimal("3.0"), null} );
    }

}
