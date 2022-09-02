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

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}

