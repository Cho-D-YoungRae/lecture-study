package study.mybatisstudy;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Insert("INSERT INTO employee(company_id, name, address)" +
            " VALUES(#{employee.companyId}, #{employee.name}, #{employee.address})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(@Param("employee") Employee employee);

    @Select("select * from employee")
    @Results(id="employeeMap", value = {
            @Result(property = "companyId", column = "company_id")
    })
    List<Employee> getAll();

    @Select("select * from employee where id=#{id}")
    @ResultMap("employeeMap")
    Employee getById(@Param("id") Long id);

    @Select("select * from employee where company_id = #{companyId}")
    List<Employee> getByCompanyId(@Param("companyId") Long companyId);
}
