package com.java.saga.order.service;

import com.example.saga.common.dto.OrderRequestDTO;
import com.example.saga.common.events.OrderEvent;
import com.example.saga.common.events.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher
{
    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;

    public void publishOrderEvent(OrderRequestDTO orderRequestDTO, OrderStatus orderStatus){
        OrderEvent orderEvent = new OrderEvent(orderRequestDTO,orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }

}
