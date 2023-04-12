package com.example.saga.payment.config;

import com.example.saga.common.events.OrderEvent;
import com.example.saga.common.events.OrderStatus;
import com.example.saga.common.events.PaymentEvent;
import com.example.saga.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class PaymentConsumerConfig {

    @Autowired
    private PaymentService paymentService;

    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor(){
        return orderEventFlux ->  orderEventFlux.flatMap(this::processPayment);
    }


    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent){
        //get user ID
        //check the balance availability
        //if balance is enough -> payment done & deduct amount from db
        //if balance is not sufficient -> cancel order event & update amount in db

        if(OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())){

            return Mono.fromSupplier(()-> this.paymentService.newOrderEvent(orderEvent));
        }
        else{
            System.out.println("Order cancelled.........................");
            return Mono.fromRunnable(()-> this.paymentService.cancelOrderEvent(orderEvent));
        }
    }
}
