package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.common.CustomException;
import com.ustlearn.dto.SetmealDto;
import com.ustlearn.pojo.*;
import com.ustlearn.service.CategoryService;
import com.ustlearn.service.SetmealDishService;
import com.ustlearn.service.SetmealService;
import com.ustlearn.mapper.SetmealMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Einstein
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2023-05-27 16:27:08
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐,同时要保存菜品和套餐的关联关系.
     *
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息,操作setmeal,执行insert操作
        this.save(setmealDto);

        //将setmealId封装在菜品里面.
        List<SetmealDish> setmealDishesList = setmealDto.getSetmealDishes();
        setmealDishesList.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息,操作setmeal_dish表,执行insert表.
        setmealDishService.saveBatch(setmealDishesList);
    }

    /**
     * 删除套餐,同时需要删除套餐和菜品的关联数据
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {

        //查询套餐状态,确实是否可用删除 状态为1,表明在售,不能删除.

        //select(*) from setmeal where id in (ids...) and status =1;
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        long count = this.count(queryWrapper);

        if (count > 0) {
            //如果不能删除,抛出一个业务异常
            throw new CustomException("套餐正在售卖中,不能删除");
        }

        //如果可以删除,先删除套餐表中的数据 --setmeal
        this.removeByIds(ids);

        //构造queryWrapper直接通过套餐id在分类中删除数据
        //delete form setmeal_dish where setmeal id in(ids...)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, ids);
        //删除关系表中的数据 --setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);

    }

    //根据id查询套餐信息和对应的菜品信息
    //回显数据
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //查询套餐信息,从setmeal表
        Setmeal setmeal = this.getById(id);
        //通过套餐id,查询菜品信息,从setmeal_dish表.
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);

        //new setmealDto对象来集成套餐信息和菜品信息
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishes);
        //通过套餐id在category中查询分类名称,注入
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getId, setmeal.getCategoryId());
        Category category = categoryService.getOne(lambdaQueryWrapper);
        String categoryName = category.getName();
        setmealDto.setCategoryName(categoryName);
        return setmealDto;
    }

    //更新套餐信息,同时更新对应的菜品信息
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //更新setmeal表的基本信息
        this.updateById(setmealDto);

        //清理当前套餐对应菜品数据--setmeal_dish表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());

        setmealDishService.remove(queryWrapper);

        //添加提交过来的菜品数据--setmeal_dish表的insert操作
        //insert后菜品有菜品id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //setmealDishes没有套餐id,循环添加套餐id
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        log.info("mealDish准备添加");
        setmealDishService.saveBatch(setmealDishes);
    }
}




