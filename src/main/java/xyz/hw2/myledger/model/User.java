package xyz.hw2.myledger.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 用户名

    @Column(nullable = false, length = 255)
    private String password; // 密码

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 注册时间

    // 默认构造方法
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    // 构造方法
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
}
