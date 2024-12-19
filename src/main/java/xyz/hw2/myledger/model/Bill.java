package xyz.hw2.myledger.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "bill")
public class Bill {

    // Getter 和 Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 账单主键

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 用户外键，关联用户表

    @Column(nullable = false, length = 100)
    private String name; // 账单名称

    @Column(name = "consume_time", nullable = false)
    private LocalDateTime consumeTime; // 消费时间

    @Column(nullable = false)
    private Double amount; // 账单金额

    // 默认构造方法
    public Bill() {
    }

    // 构造方法
    public Bill(User user, String name, LocalDateTime consumeTime, Double amount) {
        this.user = user;
        this.name = name;
        this.consumeTime = consumeTime;
        this.amount = amount;
    }

}