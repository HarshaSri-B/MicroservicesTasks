package com.java.saga.order.config;

import com.example.saga.common.dto.OrderRequestDTO;
import com.example.saga.common.events.OrderStatus;
import com.example.saga.common.events.PaymentStatus;
import com.java.saga.order.entity.PurchaseOrder;
import com.java.saga.order.repository.OrderRepository;
import com.java.saga.order.service.OrderStatusPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Configuration
public class OrderStatusUpdateHandler {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderStatusPublisher publisher;

    @Transactional
    public void updateOrder(int id, Consumer<PurchaseOrder> consumer) {
        repository.findById(id).ifPresent(consumer.andThen(this::updateOrder));
    }

    private void updateOrder(PurchaseOrder purchaseOrder) {

        boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
        OrderStatus orderStatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;

        purchaseOrder.setOrderStatus(orderStatus);
        if (!isPaymentComplete) {
            publisher.publishOrderEvent(convertEntityToDto(purchaseOrder), orderStatus);
        }
    }

    public OrderRequestDTO convertEntityToDto(PurchaseOrder purchaseOrder) {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setOrderId(purchaseOrder.getId());
        orderRequestDTO.setUserId(purchaseOrder.getUserId());
        orderRequestDTO.setAmount(purchaseOrder.getPrice());
        orderRequestDTO.setProductId(purchaseOrder.getProductId());

        return orderRequestDTO;
    }


}
