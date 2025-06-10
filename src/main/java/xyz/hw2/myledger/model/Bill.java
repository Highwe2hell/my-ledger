package xyz.hw2.myledger.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Bill类代表一个账单条目，包含账单的相关信息如名称、金额、消费时间和所属用户
 */
@Setter
@Getter
@Entity
@Table(name = "bill")
public class Bill {

    // 账单主键，由数据库自动生成
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 用户外键，每个账单必须关联一个用户，不允许为空
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 账单名称，每个账单必须有名称，最大长度为100字符，不允许为空
    @Column(nullable = false, length = 100)
    private String name;

    // 消费时间，记录账单的消费时间，不允许为空
    @Column(name = "consume_time", nullable = false)
    private LocalDateTime consumeTime;

    // 账单金额，记录消费的金额，不允许为空
    @Column(nullable = false)
    private Double amount;

    // 默认构造方法，用于创建一个空的账单对象
    public Bill() {
    }

    /**
     * 构造方法，用于创建一个完整的账单对象
     *
     * @param user        账单关联的用户对象
     * @param name        账单名称
     * @param consumeTime 消费时间
     * @param amount      账单金额
     */
    public Bill(User user, String name, LocalDateTime consumeTime, Double amount) {
        this.user = Objects.requireNonNull(user, "User cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.consumeTime = Objects.requireNonNull(consumeTime, "Consume time cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
    }
}
