package xyz.hw2.myledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.hw2.myledger.model.Bill;

import java.util.List;

/**
 * BillRepository接口继承JpaRepository，用于操作Bill实体类的数据
 * 提供了基础的CRUD操作外，还定义了根据用户ID查询账单的方法
 */
public interface BillRepository extends JpaRepository<Bill, Long> {
    /**
     * 根据用户ID查询账单
     *
     * @param userId 用户ID
     * @return 用户的账单列表
     */
    List<Bill> findByUserId(Long userId);

    /**
     * 根据用户ID和账单名称模糊查询
     *
     * @param userId 用户ID
     * @param name 账单名称的部分或全部内容
     * @return 符合条件的账单列表
     */
    List<Bill> findByUserIdAndNameContaining(Long userId, String name);
}
