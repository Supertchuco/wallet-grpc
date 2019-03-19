package unit.service;

import com.wallet.walletserver.exception.AmountIsZeroException;
import com.wallet.walletserver.service.UserService;
import com.wallet.walletserver.service.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {


    }

    @Test//(expected = AmountIsZeroException.class)
    public void checkIfAmountIsZeroWhenInputIsZeroTest(){

    }

    @Test//(expected = AmountIsZeroException.class)
    public void checkIfAmountIsZeroWhenInputIsNullTest(){

    }

    @Test//(expected = AmountIsZeroException.class)
    public void checkIfAmountIsZeroWhenInputIsNotZeroTest(){

    }

    @Test//(expected = AmountIsZeroException.class)
    public void  checkIfBalanceIsEnoughToWithdrawOperationWhenFundsAreEnoughTest(){

    }

    @Test//(expected = AmountIsZeroException.class)
    public void  checkIfBalanceIsEnoughToWithdrawOperationWhenFundsNotEnoughTest(){

    }


}
