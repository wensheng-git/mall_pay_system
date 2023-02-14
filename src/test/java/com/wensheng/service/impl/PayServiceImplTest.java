package com.wensheng.service.impl;

import com.lly835.bestpay.model.PayResponse;
import com.wensheng.service.IPayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PayServiceImplTest {
   @Autowired
    IPayService payService;
    @Test
    void create() {
        PayResponse payResponse = payService.create("12314", new BigDecimal("0.01"));
        System.out.println(payResponse);
    }

    @Test
    void asyNotify() {
    }

    @Test
    void queryByOrderId() {
    }
}