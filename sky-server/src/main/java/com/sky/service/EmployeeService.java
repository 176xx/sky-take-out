package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.ArrayList;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addemployee(EmployeeDTO employeeDTO);

    PageResult selectEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    void openORstopemployee(Integer status,Integer id);

    void updateEmployee(EmployeeDTO employeeDTO);

    Employee selectById(Integer id);
}
