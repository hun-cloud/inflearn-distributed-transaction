package com.example.monolithic.point.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long amount;

    public Point(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public void use(Long amount) {
        if (this.amount < amount) {
            throw new RuntimeException("잔액이 부족합니다.");
        }

        this.amount = this.amount - amount;
    }

}
