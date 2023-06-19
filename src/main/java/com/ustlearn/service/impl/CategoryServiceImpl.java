package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.common.CustomException;
import com.ustlearn.mapper.CategoryMapper;
import com.ustlearn.pojo.Category;
import com.ustlearn.pojo.Dish;
import com.ustlearn.pojo.Setmeal;
import com.ustlearn.service.CategoryService;
import com.ustlearn.service.DishService;
import com.ustlearn.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    /**
     * 根据id删除分类,删除之前进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件,根据id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品,如果已经关联,抛出一个业务异常
        if (count1 > 0) {
            //已经关联菜品,抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }

        //查询当前分类是否关联了套餐,如果已经关联,抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件,根据id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count2 = dishService.count(dishLambdaQueryWrapper);

        if (count2 > 0) {
            //已经关联套餐,抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }

        //正常删除分类
        super.removeById(id);


    }
}
