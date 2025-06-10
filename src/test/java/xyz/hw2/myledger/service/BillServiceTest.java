package xyz.hw2.myledger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import xyz.hw2.myledger.model.Bill;
import xyz.hw2.myledger.model.User;
import xyz.hw2.myledger.repository.BillRepository;
import xyz.hw2.myledger.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * BillService 单元测试类
 */
class BillServiceTest {

    @InjectMocks
    private BillService billService;

    @Mock
    private BillRepository billRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试 createBill 方法
     * 场景1：用户存在，成功创建账单
     */
    @Test
    void testCreateBill_UserExists() {
        Long userId = 1L;
        String name = "Dinner";
        String consumeTime = "2025-04-05T19:00:00";
        Double amount = 100.0;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bill result = billService.createBill(userId, name, consumeTime, amount);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(LocalDateTime.parse(consumeTime), result.getConsumeTime());
        assertEquals(amount, result.getAmount());
        verify(userRepository, times(1)).findById(userId);
        verify(billRepository, times(1)).save(result);
    }

    /**
     * 测试 createBill 方法
     * 场景2：用户不存在，抛出异常
     */
    @Test
    void testCreateBill_UserNotFound() {
        Long userId = 1L;
        String name = "Dinner";
        String consumeTime = "2025-04-05T19:00:00";
        Double amount = 100.0;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                billService.createBill(userId, name, consumeTime, amount));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(billRepository, never()).save(any(Bill.class));
    }

    /**
     * 测试 getBillsByUserId 方法
     * 场景1：用户有账单
     */
    @Test
    void testGetBillsByUserId_WithBills() {
        Long userId = 1L;
        List<Bill> bills = Arrays.asList(new Bill(), new Bill());

        when(billRepository.findByUserId(userId)).thenReturn(bills);

        List<Bill> result = billService.getBillsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(billRepository, times(1)).findByUserId(userId);
    }

    /**
     * 测试 getBillsByUserId 方法
     * 场景2：用户无账单
     */
    @Test
    void testGetBillsByUserId_NoBills() {
        Long userId = 1L;

        when(billRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<Bill> result = billService.getBillsByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(billRepository, times(1)).findByUserId(userId);
    }

    /**
     * 测试 deleteBill 方法
     * 场景：删除账单
     */
    @Test
    void testDeleteBill() {
        Long billId = 1L;

        doNothing().when(billRepository).deleteById(billId);

        billService.deleteBill(billId);

        verify(billRepository, times(1)).deleteById(billId);
    }

    /**
     * 测试 getBillById 方法
     * 场景1：账单存在
     */
    @Test
    void testGetBillById_BillExists() {
        Long billId = 1L;
        Bill bill = new Bill();

        when(billRepository.findById(billId)).thenReturn(Optional.of(bill));

        Optional<Bill> result = billService.getBillById(billId);

        assertTrue(result.isPresent());
        assertEquals(bill, result.get());
        verify(billRepository, times(1)).findById(billId);
    }

    /**
     * 测试 getBillById 方法
     * 场景2：账单不存在
     */
    @Test
    void testGetBillById_BillNotFound() {
        Long billId = 1L;

        when(billRepository.findById(billId)).thenReturn(Optional.empty());

        Optional<Bill> result = billService.getBillById(billId);

        assertFalse(result.isPresent());
        verify(billRepository, times(1)).findById(billId);
    }

    /**
     * 测试 updateBill 方法
     * 场景1：账单存在，成功更新
     */
    @Test
    void testUpdateBill_BillExists() {
        Long billId = 1L;
        String newName = "Updated Dinner";
        String newConsumeTime = "2025-04-06T20:00:00";
        Double newAmount = 150.0;

        Bill existingBill = new Bill();
        existingBill.setId(billId);
        existingBill.setName("Old Name");

        when(billRepository.findById(billId)).thenReturn(Optional.of(existingBill));
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bill result = billService.updateBill(billId, newName, newConsumeTime, newAmount);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(LocalDateTime.parse(newConsumeTime), result.getConsumeTime());
        assertEquals(newAmount, result.getAmount());
        verify(billRepository, times(1)).findById(billId);
        verify(billRepository, times(1)).save(result);
    }

    /**
     * 测试 updateBill 方法
     * 场景2：账单不存在，抛出异常
     */
    @Test
    void testUpdateBill_BillNotFound() {
        Long billId = 1L;
        String newName = "Updated Dinner";
        String newConsumeTime = "2025-04-06T20:00:00";
        Double newAmount = 150.0;

        when(billRepository.findById(billId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                billService.updateBill(billId, newName, newConsumeTime, newAmount));

        assertEquals("Bill not found", exception.getMessage());
        verify(billRepository, times(1)).findById(billId);
        verify(billRepository, never()).save(any(Bill.class));
    }

    /**
     * 测试 getBillsByUserIdAndName 方法
     * 场景1：找到匹配账单
     */
    @Test
    void testGetBillsByUserIdAndName_WithResults() {
        Long userId = 1L;
        String name = "Dinner";
        List<Bill> bills = Arrays.asList(new Bill(), new Bill());

        when(billRepository.findByUserIdAndNameContaining(userId, name)).thenReturn(bills);

        List<Bill> result = billService.getBillsByUserIdAndName(userId, name);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(billRepository, times(1)).findByUserIdAndNameContaining(userId, name);
    }

    /**
     * 测试 getBillsByUserIdAndName 方法
     * 场景2：无匹配账单
     */
    @Test
    void testGetBillsByUserIdAndName_NoResults() {
        Long userId = 1L;
        String name = "NonExistent";

        when(billRepository.findByUserIdAndNameContaining(userId, name)).thenReturn(Collections.emptyList());

        List<Bill> result = billService.getBillsByUserIdAndName(userId, name);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(billRepository, times(1)).findByUserIdAndNameContaining(userId, name);
    }
}
