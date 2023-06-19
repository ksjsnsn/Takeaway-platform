package com.ustlearn.mapper;

import com.ustlearn.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Einstein
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2023-05-27 16:26:54
* @Entity com.ustlearn.pojo.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




