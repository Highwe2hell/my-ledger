package xyz.hw2.myledger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.hw2.myledger.model.User;
import xyz.hw2.myledger.repository.UserRepository;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 注册方法
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

    // 登录方法
    public User login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        }
        throw new RuntimeException("Invalid username or password");
    }
}
