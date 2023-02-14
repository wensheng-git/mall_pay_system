package com.wensheng.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wensheng.entity.MallPayInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 86159
* @description 针对表【mall_pay_info】的数据库操作Mapper
* @createDate 2022-09-30 11:54:34
* @Entity com.wensheng.entity.MallPayInfo
*/
@Mapper
@Repository
public interface MallPayInfoMapper extends BaseMapper<MallPayInfo> {
    MallPayInfo selectByOrderNo(@Param("orderNo") Long orderNo);
    int updateByPayId(MallPayInfo mallPayInfo);
}




