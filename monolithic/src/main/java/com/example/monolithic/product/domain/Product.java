package com.example.monolithic.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;
    private Long price;

    private Long reservedQuantity;

    @Version
    private Long version;

    public Product(Long quantity, Long price) {
        this.quantity = quantity;
        this.price = price;
    }

    public Long reserve(Long requestQuantity) {
        long reservableQuantity = this.quantity - this.reservedQuantity;

        if (reservableQuantity < requestQuantity) {
            throw new RuntimeException("예약할 수 있는 수량이 부족합니다.");
        }

        reservedQuantity += requestQuantity;

        return price * requestQuantity;
    }

    public void cancel(Long requestQuantity) {
         if (this.reservedQuantity < requestQuantity) {
             throw new RuntimeException("예약된 수량이 부족합니다.");
         }

        this.reservedQuantity -= requestQuantity;
    }

    public void confirm(Long requestQuantity) {
        if (this.quantity < requestQuantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        if (this.reservedQuantity < requestQuantity) {
            throw new RuntimeException("예약된 수량이 부족합니다.");
        }

        this.quantity -= requestQuantity;
        this.reservedQuantity -= requestQuantity;
    }

    public Long calculatePrice(Long quantity) {
        return price * quantity;
    }

    public void buy(Long quantity) {
        if (this.quantity < quantity) {
            throw new RuntimeException("재고가 부족합니다");
        }

        this.quantity = this.quantity - quantity;
    }

}
