package com.wensheng.service.impl;
import com.google.gson.Gson;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.wensheng.entity.MallPayInfo;
import com.wensheng.mapper.MallPayInfoMapper;
import com.wensheng.service.IPayService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PayServiceImpl implements IPayService {
    @Autowired
    BestPayService bestPayService;
    @Autowired
    MallPayInfoMapper mallPayInfoMapper;
    @Autowired
    AmqpTemplate amqpTemplate;
    private final static String QUEUE_PAY_NOTIFY = "payNotify";


    @Override
    public PayResponse create(String OrderId, BigDecimal amount) {
        // 1:先写入订单到数据库(先校验orderId的记录是否存在,只刷新二维码不支付会发起多次的插入数据请求)
        MallPayInfo mallPayInfo = mallPayInfoMapper.selectByOrderNo(Long.valueOf(OrderId));
        if (mallPayInfo == null) {
            MallPayInfo payInfo = MallPayInfo.builder().payAmount(amount)
                    .createTime(new Date()).payPlatform(1)
                    .platformStatus(OrderStatusEnum.NOTPAY.name())
                    .orderNo(Long.valueOf(OrderId))
                    .build();
            try {
                mallPayInfoMapper.insert(payInfo);
            } catch (Exception e) {
                //TODO 报警
                throw new RuntimeException("订单插入异常");
            }
        }
        // 2:发起支付请求
        PayRequest payRequest = new PayRequest();
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);
        payRequest.setOrderId(OrderId);
        payRequest.setOrderName("微信公众账号支付订单");
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);

        PayResponse response = bestPayService.pay(payRequest);
        return response;
    }


    @Override
    public String asyNotify(String notifyData) {
        // 1:签名校验------->bestService(可以发起通知也可以查看异步回调)
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);

        // 2:金额校验(根据微信异步传来的orderNo)
        MallPayInfo mallPayInfo = mallPayInfoMapper.selectByOrderNo(Long.valueOf(payResponse.getOrderId()));
        if(mallPayInfo==null){
            //TODO 报警
            throw new RuntimeException("该订单号在数据库中不存在,数据库可能被篡改");
        }else if(mallPayInfo.getPlatformStatus().compareTo(OrderStatusEnum.NOTPAY.name())!=0){
            //TODO 金额校验
            //tip:new Decimal的时候要选择字符串否则精度会丢失
            if (!mallPayInfo.getPayAmount().equals(new BigDecimal(String.valueOf(payResponse.getOrderAmount())))) {
                throw new RuntimeException("支付金额和数据库中不一致,订单号为"+payResponse.getOrderId());
            }
        }
        mallPayInfo.setPlatformNumber(payResponse.getOutTradeNo());
        mallPayInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
        mallPayInfoMapper.updateByPayId(mallPayInfo);


        //TODO 3:发送mq消息,通知mall已支付完成
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY,new Gson().toJson(mallPayInfo));

        // 4:停止微信继续回调
        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }




    @Override
    public MallPayInfo queryByOrderId(Long orderId) {
        return mallPayInfoMapper.selectByOrderNo(orderId);
    }
}
