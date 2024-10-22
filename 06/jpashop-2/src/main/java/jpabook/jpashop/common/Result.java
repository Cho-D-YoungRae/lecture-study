package jpabook.jpashop.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private boolean success;

    private T data;

    private String failCode;

    private String message;
}
