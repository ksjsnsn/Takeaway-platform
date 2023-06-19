package com.ustlearn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ustlearn.common.Result;
import com.ustlearn.dto.DishDto;
import com.ustlearn.dto.SetmealDto;
import com.ustlearn.pojo.Category;
import com.ustlearn.pojo.Setmeal;
import com.ustlearn.pojo.SetmealDish;
import com.ustlearn.service.CategoryService;
import com.ustlearn.service.SetmealDishService;
import com.ustlearn.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return Result.success("保存菜品成功");
    }

    /**
     * 套餐信息分页查询
     *
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        //setMeal 对象中没有套餐分类字段
        //构造dto对象
        Page<SetmealDto> dtoPage = new Page<>();

        //查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //name字段like模糊匹配查询
        queryWrapper.like(name != null, Setmeal::getName, name);
        //添加排序条件,更新时间降序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //重构DtoPage中records数据,加上categoryName字段
        //不能拷贝records,因为封装的数据对象不一样,前者为setMeal,后者为setmealDto.
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        //重构一个来接收records数据
        List<SetmealDto> collect = records.stream().map((item) -> {
            //新建对象来copy setmeal对象的其它值,再设置categoryName的值
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            //分类ID
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        //塞回records
        dtoPage.setRecords(collect);
        return Result.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("接收到ids: {}", ids);
        setmealService.removeWithDish(ids);
        return Result.success("套餐数据删除成功");
    }

    /**
     * 通过id查询套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> update(@PathVariable Long id) {
        log.info("获取要查询的id为:{}", id);
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return Result.success(setmealDto);
    }


    //停售起售大失败,和前端交互没做好.
    //详细说明:
    //不能一次操作一个套餐,前端发送的url中的0/1如何写在一个方法里面.
    //等待再次研究.
/*    @PostMapping("//status/0")
    public Result<String> stopSell(Long ids) {
        log.info("停售的套餐id为:{}", ids);
        //设置为停售状态 1:在售 0:停售
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Setmeal::getStatus, 0);
        setmealService.update(updateWrapper);
        return Result.success("停售成功");
    }

    @PostMapping("//status/1")
    public Result<String> openSell(Long ids) {
        log.info("停售的套餐id为:{}", ids);
        //设置为停售状态 1:在售 0:停售
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Setmeal::getStatus, 1);
        setmealService.update(updateWrapper);
        return Result.success("起售成功");
    }*/

    /**
     * 修改套餐
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("修改菜品");
        log.info(setmealDto.toString());

        //操作两张表
        setmealService.updateWithDish(setmealDto);
        return Result.success("修改套餐成功");
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal) {
        //构造查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        return Result.success(setmealList);
    }

}
