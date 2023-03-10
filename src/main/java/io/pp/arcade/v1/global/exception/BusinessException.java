package io.pp.arcade.v1.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private String code;
    private String message;

    public BusinessException(String code) {
        this.code = code;
        this.message = "잘못된 요청입니다.";
    }
}

