package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.UserServiceTest;
import rs.ac.bg.fon.repository.RoleRepository;
import rs.ac.bg.fon.repository.UserRepository;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest extends UserServiceTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private RoleRepository roleRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private PaymentService paymentServiceMock;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = passwordEncoderMock;
        this.userRepository = userRepositoryMock;
        this.roleRepository = roleRepositoryMock;
        this.paymentService = paymentServiceMock;
        this.userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder, paymentService);
    }

    @AfterEach
    void tearDown() {
        this.userService = null;
    }

}
