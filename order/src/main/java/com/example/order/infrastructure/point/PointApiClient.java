package com.example.order.infrastructure.point;

import com.example.order.infrastructure.point.dto.PointReserveApiRequest;
import com.example.order.infrastructure.point.dto.PointReserveCancelApiRequest;
import com.example.order.infrastructure.point.dto.PointReserveConfirmApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class PointApiClient {

    private final RestClient restClient;

    @Retryable(
            retryFor = {Exception.class},
            noRetryFor = {
                    HttpClientErrorException.BadRequest.class,
                    HttpClientErrorException.NotFound.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 1.5, maxDelay = 5000) // 500ms delay, 1.5x multiplier, max 5s delay
    )
    public void reservePoint(PointReserveApiRequest request) {
        restClient.post()
                .uri("/point/reserve")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void confirmPoint(PointReserveConfirmApiRequest request) {
        restClient.post()
                .uri("/point/confirm")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void cancelPoint(PointReserveCancelApiRequest request) {
        restClient.post()
                .uri("/point/cancel")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
