package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.mapper.SetmealDishMapper;
import com.ustlearn.pojo.SetmealDish;
import com.ustlearn.service.SetmealDishService;
import com.ustlearn.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
