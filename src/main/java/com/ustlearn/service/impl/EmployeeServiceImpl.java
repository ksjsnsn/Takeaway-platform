package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.pojo.Employee;
import com.ustlearn.service.EmployeeService;
import com.ustlearn.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
 * @author Einstein
 * @description 针对表【employee(员工信息)】的数据库操作Service实现
 * @createDate 2023-05-24 17:21:22
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

}




