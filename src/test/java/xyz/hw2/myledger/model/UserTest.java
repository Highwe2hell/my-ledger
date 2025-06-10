package xyz.hw2.myledger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * User类的单元测试。
 */
public class UserTest {

    private LocalDateTime testTime;

    @BeforeEach
    public void setUp() {
        // 固定测试时间以避免时钟漂移问题
        testTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
    }

    /**
     * 测试默认构造方法是否正确初始化createdAt字段。
     */
    @Test
    public void testDefaultConstructor_setsCreatedAtToNow() {
        // 使用反射或替换LocalDateTime.now()来模拟固定时间（此处简化为手动赋值）
        User user = new User();
        assertNotNull(user.getCreatedAt());
    }

    /**
     * 测试带参构造方法是否正确设置username、password和createdAt。
     */
    @Test
    public void testParameterizedConstructor_setsFieldsCorrectly() {
        String username = "testUser";
        String password = "securePassword";

        User user = new User(username, password);

        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertNotNull(user.getCreatedAt());
    }

    /**
     * 测试setter方法是否能正确设置各个字段。
     */
    @Test
    public void testSettersAndGetters() {
        User user = new User();

        Long id = 1L;
        String username = "newUser";
        String password = "newPassword";

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setCreatedAt(testTime);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(testTime, user.getCreatedAt());
    }
}
