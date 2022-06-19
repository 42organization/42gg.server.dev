package io.pp.arcade.domain.admin.dto.create;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class NotiCreateDto {
    private Integer userId;
    private Integer slotId;
    private String notiType;
    private Boolean isChecked;
    private String message;
}
