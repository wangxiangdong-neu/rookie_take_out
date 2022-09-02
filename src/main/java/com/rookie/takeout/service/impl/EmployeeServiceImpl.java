package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.entity.Employee;
import com.rookie.takeout.mapper.EmployeeMapper;
import com.rookie.takeout.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @title: EmployeeServiceImpl
 * @Author: Mrdong
 * @Date: 2022/8/26 17:05
 * @Description:
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
