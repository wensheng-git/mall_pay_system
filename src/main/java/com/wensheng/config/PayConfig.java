package com.wensheng.config;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {
  @Autowired
  WxParamConfig wxParamConfig;
  @Bean
  public BestPayService  configService(WxParamConfig wxParamConfig){
    // wx支付配置
    WxPayConfig wxPayConfig = new WxPayConfig();
    wxPayConfig.setAppId(wxParamConfig.getAppId());// appId
    wxPayConfig.setMchId(wxParamConfig.getMchId());// 商户Id
    wxPayConfig.setMchKey(wxParamConfig.getMchKey());// 商户秘钥
    wxPayConfig.setNotifyUrl(wxParamConfig.getNotifyUrl());// 回调地址(验证签名和金额来修改支付状态)
    wxPayConfig.setReturnUrl(wxParamConfig.getReturnUrl());// 返回地址

    // 配置加入到发起支付的service
    BestPayServiceImpl bestPayService = new BestPayServiceImpl();
    bestPayService.setWxPayConfig(wxPayConfig);
    return bestPayService;
  }

}
