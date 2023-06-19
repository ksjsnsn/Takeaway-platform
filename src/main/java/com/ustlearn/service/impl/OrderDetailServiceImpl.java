package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.pojo.OrderDetail;
import com.ustlearn.service.OrderDetailService;
import com.ustlearn.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author Einstein
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-06-06 10:59:23
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




