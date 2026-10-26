package study.mybatisstudy;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CompanyMapper {

    // @Insert 에서 입력에 성공한 데이터 개수 반환
    @Insert("INSERT INTO company(name, address) VALUES(#{company.name}, #{company.address})")
    int insertV1(@Param("company") Company company);

    // 쿼리에 입력되는 id 는 쿼리가 날라가면서 생성된다.
    // 이를 반환받기 위해서는 아래 @Options 와 같은 세팅이 필요하다.
    // 해당 세팅을 하면 자동 생성되는 key 값을 객체의 "id" 에 넣어준다.
    @Insert("INSERT INTO company(name, address) VALUES(#{company.name}, #{company.address})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(@Param("company") Company company);

    // table 칼럼명과 객체의 프로퍼티명이 다르면 아래와 같이 매핑해주어야 한다.
    // 아래 매핑에 id 를 설정해주면 재활용 가능하다.
//    @Results(id="companyMap", value = {
//            @Result(property = "name", column = "company_name"),
//            @Result(property = "address", column = "company_address")
//    })
    @Select("select * from company")
    List<Company> getAllV1();

    // CompanyService.getAll() 에서 처럼 서비스 층을 컨트롤러와 매퍼 사이에 두어서
    // employees 를 만드는 로직을 둘 수도 있지만, 매퍼에서 다 해줄 수도 있다.
    @Select("select * from company")
    @Results(id = "companyMap", value = {
            @Result(property = "employees", column = "id", many = @Many(select = "study.mybatisstudy.EmployeeMapper.getByCompanyId"))
    })
    List<Company> getAll();

    // 위에서 정의한 @Results 매핑을 재활용 할 수 있다.
    @ResultMap("companyMap")
    @Select("select * from company where id=#{id}")
    Company getById(@Param("id") Long id);
}
