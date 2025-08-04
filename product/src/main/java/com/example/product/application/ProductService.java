package com.example.product.application;

import com.example.monolithic.product.domain.Product;
import com.example.monolithic.product.infrastructure.ProductRepository;
import com.example.product.application.dto.ProductReserveCommand;
import com.example.product.application.dto.ProductReserveResult;
import com.example.product.domain.ProductReservation;
import com.example.product.infrastructure.ProductReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductReservationRepository productReservationRepository;

    @Transactional
    public ProductReserveResult tryReserve(ProductReserveCommand command) {
        List<ProductReservation> exists = productReservationRepository.findAllByRequestId(command.requestId());

        if (!exists.isEmpty()) {
            long totalPrice = exists.stream().mapToLong(ProductReservation::getReservedPrice).sum();

            return new ProductReserveResult(totalPrice);
        }

        Long totalPrice = 0L;
        for (ProductReserveCommand.ReserveItem item : command.items()) {
            Product product = productRepository.findById(item.productId()).orElseThrow();

            Long price = product.reserve(item.reserveQuantity());
            totalPrice += price;

            productRepository.save(product);
            productReservationRepository.save(
                    new ProductReservation(
                            command.requestId(),
                            item.productId(),
                            item.reserveQuantity(),
                            price
                    )
            );
        }

        return new ProductReserveResult(totalPrice);
    }

}
