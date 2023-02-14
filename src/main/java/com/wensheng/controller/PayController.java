package com.wensheng.controller;

import com.lly835.bestpay.model.PayResponse;
import com.wensheng.config.WxParamConfig;
import com.wensheng.entity.MallPayInfo;
import com.wensheng.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    IPayService payService;
    @Autowired
    WxParamConfig wxParamConfig;
    Logger logger= Logger.getGlobal();

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount
    ){

        // 思路:浏览器发起请求带参数=====调用PayService请求第三方支付====接受的返回codeUrl给浏览器渲染===返回对应的二维码
        PayResponse response = payService.create(orderId, amount);
        Map map=new HashMap<>();
        map.put("codeUrl",response.getCodeUrl());
        map.put("orderId",orderId);
        map.put("returnUrl",wxParamConfig.getReturnUrl());
        // 支付成功后页面轮循查询支付状态..已经支付就直接跳转
        ModelAndView modelAndView = new ModelAndView("create",map);
        return modelAndView;
    }





    // 支付成功后,微信会向回调地址间隔发送回调该接口
    @PostMapping("/notify")
    @ResponseBody
    public String asyNotify(@RequestBody String notifyData){
        logger.info("wx异步回调已经到达,等待修改订单状态中");
        return payService.asyNotify(notifyData);
    }





    // 二维码界面不断轮循该接口查看支付状态完成跳转
    @RequestMapping("/query")
    @ResponseBody
    public MallPayInfo query(@RequestParam("orderId") String orderId){
        return payService.queryByOrderId(Long.valueOf(orderId));
    }

}
