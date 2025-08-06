package com.example.monolithic.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public enum OrderStatus {
        CREATED, RESERVED, COMPLETED, CONFIRMED, CANCELED, PENDING
    }

    public void complete() {
        status = OrderStatus.COMPLETED;
    }

    public void reserve() {
        if (this.status != OrderStatus.CREATED) {
            throw new RuntimeException("생성된 단계에서만 예약할 수 있습니다.");
        }

        this.status = OrderStatus.RESERVED;
    }

    public void cancel() {
        if (this.status != OrderStatus.RESERVED) {
            throw new RuntimeException("예약된 단계에서만 취소할 수 있습니다.");
        }

        this.status = OrderStatus.CANCELED;
    }

    public void confirm() {
        if (this.status != OrderStatus.RESERVED && this.status != OrderStatus.PENDING) {
            throw new RuntimeException("예약된 단계 혹은 Pending 확정할 수 있습니다.");
        }

        this.status = OrderStatus.CONFIRMED;
    }

    public void pending() {
        if (this.status != OrderStatus.RESERVED) {
            throw new RuntimeException("예약된 단계에서만 확정할 수 있습니다.");
        }

        this.status = OrderStatus.PENDING;
    }
}
