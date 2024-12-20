package xyz.hw2.myledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.hw2.myledger.model.Bill;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByUserId(Long userId);

    List<Bill> findByUserIdAndNameContaining(Long userId, String name);  // 根据账单名称模糊查询
}
