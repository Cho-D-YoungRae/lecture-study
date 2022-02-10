package study.mybatisstudy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Employee {
    private Long id;
    private Long companyId;
    private String name;
    private String address;
}
