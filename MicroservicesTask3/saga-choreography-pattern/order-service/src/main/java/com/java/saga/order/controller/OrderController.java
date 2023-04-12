package com.java.saga.order.controller;

import com.example.saga.common.dto.OrderRequestDTO;
import com.java.saga.order.entity.PurchaseOrder;
import com.java.saga.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public PurchaseOrder createOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        return orderService.createOrder(orderRequestDTO);
    }

    @GetMapping
    public List<PurchaseOrder> getOrders(){
        return orderService.getAllOrders();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrders(@PathVariable Integer id){
        orderService.deleteOrderById(id);
        return "Order Cancelled";
    }
}
