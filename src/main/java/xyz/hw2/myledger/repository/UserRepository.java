package xyz.hw2.myledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.hw2.myledger.model.User;

import java.util.Optional;

/**
 * 用户仓库接口，继承自JpaRepository以获取用户数据访问对象的标准行为
 * 该接口用于定义用户相关的数据库操作，特别是通过用户名查找用户的功能
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据用户名查找用户
     *
     * @param username 用户名，用于查询用户记录的唯一标识
     * @return 如果找到匹配的用户，则返回包含用户对象的Optional，否则返回空Optional
     */
    Optional<User> findByUsername(String username);
}
