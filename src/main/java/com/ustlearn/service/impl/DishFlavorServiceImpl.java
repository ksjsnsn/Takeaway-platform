package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.pojo.DishFlavor;
import com.ustlearn.service.DishFlavorService;
import com.ustlearn.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author Einstein
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-05-28 09:58:28
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




