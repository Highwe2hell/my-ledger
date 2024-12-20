package xyz.hw2.myledger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.hw2.myledger.model.User;
import xyz.hw2.myledger.repository.UserRepository;

import jakarta.transaction.Transactional;
import java.util.Optional;

/**
 * 用户服务类，提供用户注册和登录功能
 */
@Service
public class UserService {

    // 用户仓库，用于用户数据的持久化操作
    @Autowired
    private UserRepository userRepository;

    /**
     * 用户注册方法
     *
     * @param username 用户名
     * @param password 密码
     * @return 注册成功的用户对象
     * @throws RuntimeException 如果用户名已存在
     */
    @Transactional
    public User register(String username, String password) {
        // 检查用户名是否已存在
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // 创建新用户并保存
        User user = new User(username, password);
        return userRepository.save(user);
    }

    /**
     * 用户登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象
     * @throws RuntimeException 如果用户名或密码无效
     */
    public User login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        }
        throw new RuntimeException("Invalid username or password");
    }
}
