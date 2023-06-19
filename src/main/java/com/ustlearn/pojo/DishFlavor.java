package com.ustlearn.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 菜品口味关系表
 *
 * @TableName dish_flavor
 */
@TableName(value = "dish_flavor")
@Data
public class DishFlavor implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 菜品
     */
    private Long dishId;

    /**
     * 口味名称
     */
    private String name;

    /**
     * 口味数据list
     */
    private String value;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private Long updateUser;

    /**
     * 是否删除
     */
    //@TableLogic
    //不进行逻辑删除判断.不好进行更新操作,
    //在进行菜品修改的操作时,因为逻辑删除之后菜品id还在,不能用前端传过来的id做新增操作.
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}