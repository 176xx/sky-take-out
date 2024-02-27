package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from sky_take_out.employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into sky_take_out.employee values (null,#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void addemployee(Employee employee);

    Page<Employee> selectEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    @Update("update sky_take_out.employee set status=#{status} where id = #{id}")
    void openORstopemployee(Integer status,Integer id);

    @Update("update sky_take_out.employee set name=#{employeeDTO.name},username=#{employeeDTO.username},sex=#{employeeDTO.sex},phone=#{employeeDTO.phone},id_number=#{employeeDTO.idNumber},update_time=#{updateTime},update_user=#{updateUser} where id = #{employeeDTO.id}")
    void updateEmployee(EmployeeDTO employeeDTO, Long updateUser, LocalDateTime updateTime);

    @Select("select * from sky_take_out.employee where id=#{id}")
    Employee selectById(Integer id);
}
