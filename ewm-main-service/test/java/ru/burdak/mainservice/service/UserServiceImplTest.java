package java.ru.burdak.mainservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.burdak.ewmstatsclient.client.StatsClient;
import ru.burdak.mainservice.repository.UserRepository;
import ru.burdak.mainservice.service.UserService;
import ru.burdak.mainservice.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {


    }
    @Test
    void shouldCreateUser() {


    }

}
