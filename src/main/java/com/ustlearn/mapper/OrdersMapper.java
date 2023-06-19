package com.ustlearn.mapper;

import com.ustlearn.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Einstein
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2023-06-06 10:59:18
* @Entity com.ustlearn.pojo.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




