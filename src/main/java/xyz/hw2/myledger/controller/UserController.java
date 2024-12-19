package xyz.hw2.myledger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.hw2.myledger.model.User;
import xyz.hw2.myledger.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            // 调用注册服务
            User newUser = userService.register(username, password);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", newUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // 如果用户名已存在，返回错误
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Username already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            User user = userService.login(username, password);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);  // 将用户信息放入响应中
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
