package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.global.type.NotiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
public class NotiCanceledDto {
    private Integer id;
    private String type;
    private LocalDateTime time;
    private Boolean isChecked;

    @Builder
    public NotiCanceledDto(Integer id, NotiType type, LocalDateTime time, Boolean isChecked, LocalDateTime createdAt) {
        this.id = id;
        this.type = type.getCode().toLowerCase(Locale.ROOT);
        this.time = time;
        this.isChecked = isChecked;
        this.createdAt = createdAt;
    }

    private LocalDateTime createdAt;
}
