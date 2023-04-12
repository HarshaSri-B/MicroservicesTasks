package com.example.saga.payment.service;

import com.example.saga.common.dto.OrderRequestDTO;
import com.example.saga.common.dto.PaymentRequestDTO;
import com.example.saga.common.events.OrderEvent;
import com.example.saga.common.events.PaymentEvent;
import com.example.saga.common.events.PaymentStatus;
import com.example.saga.payment.entity.UserBalance;
import com.example.saga.payment.entity.UserTransaction;
import com.example.saga.payment.repository.UserBalanceRepository;
import com.example.saga.payment.repository.UserTransactionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @PostConstruct
    public void initUserBalance() {
        userBalanceRepository.saveAll(
                Stream.of(new UserBalance(101, 5000),
                        new UserBalance(102, 3657),
                        new UserBalance(103, 7000),
                        new UserBalance(104, 3234),
                        new UserBalance(105, 8000),
                        new UserBalance(106, 1657)).collect(Collectors.toList())
        );
    }

    /**
     * //get user ID
     * //check the balance availability
     * //if balance is enough -> payment done & deduct amount from db
     * //if balance is not sufficient -> cancel order event & update amount in db
     **/
    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDTO orderRequestDTO = orderEvent.getOrderRequestDTO();
        logger.info(orderRequestDTO.toString());
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(orderRequestDTO.getOrderId(),
                orderRequestDTO.getUserId(), orderRequestDTO.getAmount());
        logger.info(paymentRequestDTO.toString());


        return userBalanceRepository.findById(orderRequestDTO.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDTO.getAmount())
                .map(ub -> {
                    ub.setPrice(ub.getPrice() - orderRequestDTO.getAmount());

                    userTransactionRepository.save(new UserTransaction(
                            orderRequestDTO.getOrderId(),
                            orderRequestDTO.getUserId(), orderRequestDTO.getAmount()));

                    return new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_FAILED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        System.out.println("2nd step checking...............");

        UserTransaction ut =  userTransactionRepository.findById(orderEvent.getOrderRequestDTO().getOrderId()).get();

        System.out.println("........................");
        System.out.println(orderEvent.getOrderRequestDTO().getUserId());
        System.out.println("........................");
        UserBalance ub = userBalanceRepository.findById(orderEvent.getOrderRequestDTO().getUserId()).get();

        ub.setPrice(ub.getPrice() + ut.getAmount());

        userTransactionRepository.deleteById(ut.getOrderId());


    }
}
