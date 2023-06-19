package com.ustlearn.service;

import com.ustlearn.dto.DishDto;
import com.ustlearn.dto.SetmealDto;
import com.ustlearn.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ustlearn.pojo.SetmealDish;

import java.util.List;

/**
 * @author Einstein
 * @description 针对表【setmeal(套餐)】的数据库操作Service
 * @createDate 2023-05-27 16:27:08
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐,同时要保存菜品和套餐的关联关系.
     *
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * 删除套餐,同时需要删除套餐和菜品的关联数据
     *
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    //根据id查询套餐信息和对应的菜品信息
    public SetmealDto getByIdWithDish(Long id);

    //更新套餐信息,同时更新对应的菜品信息
    void updateWithDish(SetmealDto setmealDto);
}
