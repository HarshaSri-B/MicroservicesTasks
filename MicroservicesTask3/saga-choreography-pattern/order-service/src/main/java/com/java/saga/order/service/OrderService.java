package com.java.saga.order.service;

import com.example.saga.common.dto.OrderRequestDTO;
import com.example.saga.common.events.OrderStatus;
import com.java.saga.order.entity.PurchaseOrder;
import com.java.saga.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDTO orderRequestDTO) {
        PurchaseOrder order = orderRepository.save(convertDtoToEntity(orderRequestDTO));

        orderRequestDTO.setOrderId(order.getId());

        //produce Kafka Event with status ORDER_CREATED;

        orderStatusPublisher.publishOrderEvent(orderRequestDTO, OrderStatus.ORDER_CREATED);

        return order;

    }

    public void deleteOrderById(Integer id) {
        PurchaseOrder purchaseOrder = orderRepository.findById(id).get();


        orderRepository.deleteById(id);

        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CANCELLED);
        orderRepository.save(purchaseOrder);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(purchaseOrder.getUserId(), purchaseOrder.getProductId(),purchaseOrder.getPrice(),purchaseOrder.getId());

        orderStatusPublisher.publishOrderEvent(orderRequestDTO,OrderStatus.ORDER_CANCELLED);


    }

    public List<PurchaseOrder> getAllOrders(){
        return orderRepository.findAll();
    }

    private PurchaseOrder convertDtoToEntity(OrderRequestDTO dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(dto.getAmount());

        return purchaseOrder;
    }


}
