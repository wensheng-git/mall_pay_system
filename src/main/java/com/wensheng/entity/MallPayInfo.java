package com.wensheng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName mall_pay_info
 */
@TableName(value ="mall_pay_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MallPayInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 支付平台:1-支付宝,2-微信
     */
    private Integer payPlatform;

    /**
     * 支付流水号
     */
    private String platformNumber;

    /**
     * 支付状态
     */
    private String platformStatus;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}