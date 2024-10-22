package study.datajpa2.dto;

import lombok.Data;

@Data
public class MemberDto {

    private final Long id;
    private final String username;
    private final String teamName;
}
