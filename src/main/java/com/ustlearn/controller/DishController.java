package com.ustlearn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ustlearn.common.Result;
import com.ustlearn.dto.DishDto;
import com.ustlearn.pojo.Category;
import com.ustlearn.pojo.Dish;
import com.ustlearn.pojo.DishFlavor;
import com.ustlearn.service.CategoryService;
import com.ustlearn.service.DishFlavorService;
import com.ustlearn.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);

        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        //page对象里面存的是分页信息
        //前端需要菜品分类值categoryName字段,dish表中没有,用page<dish>查完dish表的pageInfo对象然后拷贝在dishDtoPage对象里.
        //不拷贝page对象里面的records集合.records集合里面存的所有拿到的dish对象,因为此时的page对象拿到的
        //dish对象没有categoryName字段.
        //page<dishDto>没有对应的mapper层,所以还是用page<dish>查询数据库
        //再补充dishDto对象的categoryName字段,使有名字,然后返回前端这个Page<dishDto>

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //改造records集合,加上categoryName字段.再塞到dishDtoPage里
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            //用来存加categoryName字段的dishDto对象
            DishDto dishDto = new DishDto();

            //将records数据除了categoryName字段其他的copy到dishDto中
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId(); //分类id


            //根据在dish表中的id查询在category表中的分类名字
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                //设置到dishDto对象中
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        //将改造好的records塞到dishDtoPage中
        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品");
        log.info(dishDto.toString());

        //操作两张表
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改菜品成功");
    }

    /**
     * 根据条件查询对应菜品
     *
     * @param dish
     * @return
     */

    /*//参数为dish更通用
    @GetMapping("/list")
    public Result<List<Dish>> list(Dish dish) {
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件,状态  0 停售 1 起售 查询状态为1
        //传过来categoryId.
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        return Result.success(list);
    }*/
    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish) {
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件,状态  0 停售 1 起售 查询状态为1
        //传过来categoryId.
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = list.stream().map((item) -> {
            //用来存加categoryName字段的dishDto对象
            DishDto dishDto = new DishDto();

            //将records数据除了categoryName字段其他的copy到dishDto中
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId(); //分类id


            //根据在dish表中的id查询在category表中的分类名字
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                //设置到dishDto对象中
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品的id
            Long dishId = item.getId();
            //根据dishId查询口味
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            //将口味信息追加到dto中
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dtoList);
    }

    //TODO 待写停售,然后测试

    /**
     * 根据id删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("要删除的菜品ids:{}", ids);
        dishService.removeWithFlavor(ids);
        return Result.success("菜品数据删除成功!");
    }

    /**
     * 根据id停售套餐,将状态置为0 为停售状态,1为起售状态
     *
     * @param ids
     */
    //待研究动态接收前端传输状态码为1或者0
/*    @PostMapping("/status/0")
    public void stop(@RequestParam List<Long> ids) {
        log.info("套餐停售");
        return;
    }*/



}
