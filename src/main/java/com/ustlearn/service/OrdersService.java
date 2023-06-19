package com.ustlearn.service;

import com.ustlearn.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Einstein
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-06-06 10:59:18
*/
public interface OrdersService extends IService<Orders> {

    /**
     *用户下单
     * @param orders
     */
    void submit(Orders orders);
}
