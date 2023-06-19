package com.ustlearn.service;

import com.ustlearn.dto.DishDto;
import com.ustlearn.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Einstein
 * @description 针对表【dish(菜品管理)】的数据库操作Service
 * @createDate 2023-05-27 16:26:54
 */
public interface DishService extends IService<Dish> {
    //新增菜品,同时插入菜品对应的口味数据,需要操作两张表:dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息,同时更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);

    //删除菜品,同时删除菜品口味
    void removeWithFlavor(List<Long> ids);
}
