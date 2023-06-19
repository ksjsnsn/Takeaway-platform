package com.ustlearn.dto;

import com.ustlearn.pojo.Dish;
import com.ustlearn.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//继承dish.保存dish的数据
@Data
public class DishDto extends Dish {

    //保存flavors中的数据.除了dish以外的数据,dish数据已经继承dish类.
    //菜品口味数据
    private List<DishFlavor> flavors = new ArrayList<>();


    private String categoryName;
    //暂时没用上,后面再用
    private Integer copies;
}
