package com.example.saga.common.dto;

import com.example.saga.common.events.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Integer userId;
    private Integer productId;
    private Integer amount;
    private Integer orderId;

    //private OrderStatus orderStatus;

}
