package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.common.CustomException;
import com.ustlearn.dto.DishDto;
import com.ustlearn.pojo.Dish;
import com.ustlearn.pojo.DishFlavor;
import com.ustlearn.pojo.Setmeal;
import com.ustlearn.pojo.SetmealDish;
import com.ustlearn.service.DishFlavorService;
import com.ustlearn.service.DishService;
import com.ustlearn.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Einstein
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2023-05-27 16:26:54
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    //用来操作表dish_flavor
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品,同时保存对应口味数据
     *
     * @param dishDto
     */
    //设计多张表操作,加事务.在启动类加注解开启事务支持 springboot默认自动开启,待测试.该注解放在service层更好
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品基本信息到菜品表dish
        //自动生成菜品id.
        this.save(dishDto);

        //前端传的flavors数据中没有菜品ID,将dish中id加入list集合中进行处理
        //获取菜品id
        Long dishId = dishDto.getId(); //菜品id

        //重新封装DishDto中保存flavors数据的list集合
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        //批量保存,有可能有多个口味,sql中也是多次保存的.对应数据库表中不同的value值.
        dishFlavorService.saveBatch(flavors);


    }

    /**
     * //根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品信息,从dish表
        Dish dish = this.getById(id);

        //查询当前菜品对应口味信息,从dish_flavor表
        //为list,忌口有多行数据.

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        //DishFlavor dishFlavor = dishFlavorService.getById(dish.getCategoryId());

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        //BeanUtils.copyProperties(dishFlavor, dishDto);
        //直接单独设值,flavor
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据--dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加提交过来的口味数据--dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        //添加dishId 是否要做存疑,等待验证前端是否传过来dishId
        //已验证. flavors数组中只有第一个参数有dishId数据项,所以同上面一样保存菜品一样
        //将dishDto中的id取出重新封装到flavors中.
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //3.
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 根据id删除菜品同时删除关联的口味
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {
        //查询菜品状态,确实是否可用删除 状态为1,表明在售,不能删除.

        //select(*) from dish where id in (ids...) and status =1;
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, 1);
        long count = this.count(queryWrapper);

        if (count > 0) {
            //如果不能删除,抛出一个业务异常
            throw new CustomException("菜品正在售卖中,不能删除");
        }

        //如果可以删除,先删除菜品表中的数据 --dish
        this.removeByIds(ids);

        //构造queryWrapper直接通过菜品id在dish_flavor中删除数据
        //delete form dish_flavor where dish_id in(ids...)
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, ids);
        //删除关系表中的数据 --dish_flavor
        List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
        dishFlavorService.removeBatchByIds(flavors);

    }
}




