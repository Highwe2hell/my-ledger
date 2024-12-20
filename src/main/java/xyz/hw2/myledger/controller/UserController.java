package xyz.hw2.myledger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.hw2.myledger.model.User;
import xyz.hw2.myledger.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器类，处理与用户相关的HTTP请求
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 自动注入用户服务类，用于处理用户注册和登录等业务逻辑
     */
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param credentials 包含用户名和密码的请求体
     * @return 返回一个包含注册结果的响应实体
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            // 调用用户服务的注册方法，尝试注册新用户
            User newUser = userService.register(username, password);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", newUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // 如果用户名已存在，捕获异常并返回错误信息
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Username already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户登录接口
     *
     * @param credentials 包含用户名和密码的请求体
     * @return 返回一个包含登录结果的响应实体
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            // 调用用户服务的登录方法，验证用户身份
            User user = userService.login(username, password);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);  // 将用户信息放入响应中
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            // 如果用户名或密码不正确，捕获异常并返回错误信息
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
