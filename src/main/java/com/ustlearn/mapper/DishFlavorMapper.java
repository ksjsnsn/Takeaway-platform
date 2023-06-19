package com.ustlearn.mapper;

import com.ustlearn.pojo.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Einstein
 * @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
 * @createDate 2023-05-28 09:58:28
 * @Entity com.ustlearn.pojo.DishFlavor
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




