package study.mybatisstudy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

// Getter 가 없으면 쿼리에 데이터가 정상적으로 입력되지 않는다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Company {

    private Long id;

    private String name;

    private String address;

    private List<Employee> employees;

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
