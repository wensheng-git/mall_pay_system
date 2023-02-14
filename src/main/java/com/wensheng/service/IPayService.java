package com.wensheng.service;

import com.lly835.bestpay.model.PayResponse;
import com.wensheng.entity.MallPayInfo;

import java.math.BigDecimal;

public interface IPayService {
    // 发起支付
    PayResponse create(String OrderId, BigDecimal amount);

    // 异步回调
    String asyNotify(String notifyData);

    // 查询订单状态方便支付完成的跳转
    MallPayInfo queryByOrderId(Long orderId);
}
