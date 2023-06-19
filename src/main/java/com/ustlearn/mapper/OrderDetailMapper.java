package com.ustlearn.mapper;

import com.ustlearn.pojo.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Einstein
 * @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
 * @createDate 2023-06-06 10:59:23
 * @Entity com.ustlearn.pojo.OrderDetail
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




