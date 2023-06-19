package com.ustlearn.mapper;

import com.ustlearn.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Einstein
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2023-05-24 17:21:22
* @Entity com.ustlearn.pojo.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




