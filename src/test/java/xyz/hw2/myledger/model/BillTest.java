package xyz.hw2.myledger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 单元测试类：BillTest
 * 测试 Bill 类的构造函数、getter 和 setter 方法
 */
class BillTest {

    private User mockUser;
    private LocalDateTime testTime;
    private Bill bill;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        mockUser = new User();
        testTime = LocalDateTime.now();
        bill = new Bill(mockUser, "Dinner", testTime, 100.0);
    }

    /**
     * 测试默认构造函数是否能够创建对象
     */
    @Test
    void testDefaultConstructor() {
        Bill defaultBill = new Bill();
        assertNotNull(defaultBill);
    }

    /**
     * 测试带参数的构造函数是否正确初始化所有字段
     */
    @Test
    void testParameterizedConstructorInitializesFieldsCorrectly() {
        assertEquals(mockUser, bill.getUser());
        assertEquals("Dinner", bill.getName());
        assertEquals(testTime, bill.getConsumeTime());
        assertEquals(100.0, bill.getAmount());
    }

    /**
     * 测试 setter 方法是否能正确修改字段值
     */
    @Test
    void testSettersUpdateFieldValues() {
        User newUser = new User();
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        String newName = "Lunch";
        Double newAmount = 50.0;

        bill.setUser(newUser);
        bill.setName(newName);
        bill.setConsumeTime(newTime);
        bill.setAmount(newAmount);

        assertEquals(newUser, bill.getUser());
        assertEquals(newName, bill.getName());
        assertEquals(newTime, bill.getConsumeTime());
        assertEquals(newAmount, bill.getAmount());
    }

    /**
     * 测试字段是否允许为 null（应不允许）
     */
    @Test
    void testNonNullableFields() {
        assertThrows(NullPointerException.class, () -> {
            new Bill(null, "Test", testTime, 100.0);
        });

        assertThrows(NullPointerException.class, () -> {
            new Bill(mockUser, null, testTime, 100.0);
        });

        assertThrows(NullPointerException.class, () -> {
            new Bill(mockUser, "Test", null, 100.0);
        });

        assertThrows(NullPointerException.class, () -> {
            new Bill(mockUser, "Test", testTime, null);
        });
    }
}
