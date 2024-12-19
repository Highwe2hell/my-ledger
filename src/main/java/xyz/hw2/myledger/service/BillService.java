package xyz.hw2.myledger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.hw2.myledger.model.Bill;
import xyz.hw2.myledger.model.User;
import xyz.hw2.myledger.repository.BillRepository;
import xyz.hw2.myledger.repository.UserRepository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    // 创建账单
    @Transactional
    public Bill createBill(Long userId, String name, String consumeTime, Double amount) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        Bill bill = new Bill();
        bill.setUser(user);
        bill.setName(name);
        bill.setConsumeTime(LocalDateTime.parse(consumeTime));  // 确保日期格式正确
        bill.setAmount(amount);

        return billRepository.save(bill);
    }

    // 获取用户的所有账单
    public List<Bill> getBillsByUserId(Long userId) {
        return billRepository.findByUserId(userId);
    }

    // 删除账单
    public void deleteBill(Long billId) {
        billRepository.deleteById(billId);
    }

    // 根据账单 ID 查询账单
    public Optional<Bill> getBillById(Long billId) {
        return billRepository.findById(billId);
    }

    // 更新账单
    @Transactional
    public Bill updateBill(Long billId, String name, String consumeTime, Double amount) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isEmpty()) {
            throw new RuntimeException("Bill not found");
        }

        Bill bill = billOptional.get();
        bill.setName(name);
        bill.setConsumeTime(LocalDateTime.parse(consumeTime));
        bill.setAmount(amount);

        return billRepository.save(bill);
    }
}