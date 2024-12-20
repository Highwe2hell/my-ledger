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

/**
 * 账单服务类，负责处理与账单相关的业务逻辑
 */
@Service
public class BillService {

    // 注入账单仓库
    @Autowired
    private BillRepository billRepository;

    // 注入用户仓库
    @Autowired
    private UserRepository userRepository;

    /**
     * 创建账单
     *
     * @param userId 用户ID
     * @param name 账单名称
     * @param consumeTime 消费时间
     * @param amount 金额
     * @return 保存后的账单实体
     * @throws RuntimeException 如果用户不存在
     */
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
        bill.setConsumeTime(LocalDateTime.parse(consumeTime));
        bill.setAmount(amount);

        return billRepository.save(bill);
    }

    /**
     * 获取用户的所有账单
     *
     * @param userId 用户ID
     * @return 账单列表
     */
    public List<Bill> getBillsByUserId(Long userId) {
        return billRepository.findByUserId(userId);
    }

    /**
     * 删除账单
     *
     * @param billId 账单ID
     */
    public void deleteBill(Long billId) {
        billRepository.deleteById(billId);
    }

    /**
     * 根据账单 ID 查询账单
     *
     * @param billId 账单ID
     * @return 账单实体
     */
    public Optional<Bill> getBillById(Long billId) {
        return billRepository.findById(billId);
    }

    /**
     * 更新账单
     *
     * @param billId 账单ID
     * @param name 账单名称
     * @param consumeTime 消费时间
     * @param amount 金额
     * @return 更新后的账单实体
     * @throws RuntimeException 如果账单不存在
     */
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

    /**
     * 根据用户ID和账单名称获取账单
     *
     * @param userId 用户ID
     * @param name 账单名称
     * @return 账单列表
     */
    public List<Bill> getBillsByUserIdAndName(Long userId, String name) {
        return billRepository.findByUserIdAndNameContaining(userId, name);
    }
}
