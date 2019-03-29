package unit.service;

import com.wallet.proto.CURRENCY;
import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.entity.Wallet;
import com.wallet.walletserver.exception.UserNotFoundException;
import com.wallet.walletserver.exception.WalletWithSpecificCurrencyNotFoundException;
import com.wallet.walletserver.repository.UserRepository;
import com.wallet.walletserver.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {

        Wallet wallet1 = new Wallet(1, new BigDecimal("100.00"), CURRENCY.EUR);
        Wallet wallet2 = new Wallet(1, new BigDecimal("40.50"), CURRENCY.GBP);
        user = new User(123, "testUser", Arrays.asList(wallet1, wallet2));
    }

    @Test
    public void findUserByIdHappyScenarioTest() {
        when(userRepository.findById(144)).thenReturn(user);
        assertEquals(user, userService.findUserById(144));
    }

    @Test
    public void validateUserHappyScenarioTest() {
        userService.validateUser(user, CURRENCY.GBP);
    }

    @Test(expected = UserNotFoundException.class)
    public void validateUserWhenUserIsNullTest() {
        userService.validateUser(null, CURRENCY.GBP);
    }

    @Test(expected = WalletWithSpecificCurrencyNotFoundException.class)
    public void validateUserWhenWalletWithSpecificCurrencyNotFound() {
        userService.validateUser(user, CURRENCY.UNRECOGNIZED);
    }

    @Test
    public void saveUserByIdHappyScenarioTest() {
        userService.saveUser(user);
    }
}
