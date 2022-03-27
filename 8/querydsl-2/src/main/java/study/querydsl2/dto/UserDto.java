package study.querydsl2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private String name;
    int age;
}
