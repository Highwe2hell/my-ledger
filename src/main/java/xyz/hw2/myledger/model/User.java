package xyz.hw2.myledger.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * User类代表系统中的用户实体。
 * 它包括用户的基本信息，如用户名、密码和注册时间。
 */
@Setter
@Getter
@Entity
@Table(name = "user")
public class User {

    /**
     * 主键，唯一标识用户。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名，必须是唯一的，不可为空。
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码，不可为空。
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * 用户注册时间，不可为空，但创建后不可更新。
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 默认构造方法，初始化注册时间为当前时间。
     */
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 构造方法，用于创建新用户时指定用户名和密码。
     *
     * @param username 用户名
     * @param password 密码
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
}
