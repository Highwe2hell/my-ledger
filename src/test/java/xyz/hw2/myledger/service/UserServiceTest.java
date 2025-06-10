package xyz.hw2.myledger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import xyz.hw2.myledger.model.User;
import xyz.hw2.myledger.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试类
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------------
    // register 方法测试
    // -----------------------------

    /**
     * TC01: 用户名已存在，注册失败
     */
    @Test
    void testRegister_UsernameAlreadyExists() {
        String username = "testuser";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, "oldpass")));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(username, password);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * TC02: 用户名不存在，注册成功
     */
    @Test
    void testRegister_SuccessfulRegistration() {
        String username = "newuser";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = userService.register(username, password);

        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(password, registeredUser.getPassword());

        verify(userRepository, times(1)).save(any(User.class));
    }

    // -----------------------------
    // login 方法测试
    // -----------------------------

    /**
     * TC03: 用户名不存在，登录失败
     */
    @Test
    void testLogin_UserNotFound() {
        String username = "nonexistent";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(username, password);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }

    /**
     * TC04: 用户名存在但密码错误，登录失败
     */
    @Test
    void testLogin_InvalidPassword() {
        String username = "testuser";
        String correctPassword = "correctpass";
        String wrongPassword = "wrongpass";

        User user = new User(username, correctPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(username, wrongPassword);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }

    /**
     * TC05: 用户名和密码正确，登录成功
     */
    @Test
    void testLogin_SuccessfulLogin() {
        String username = "testuser";
        String password = "password123";

        User user = new User(username, password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User loggedInUser = userService.login(username, password);

        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        assertEquals(password, loggedInUser.getPassword());
    }
}
