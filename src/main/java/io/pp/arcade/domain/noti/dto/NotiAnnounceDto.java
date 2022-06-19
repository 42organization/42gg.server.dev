package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.global.type.NotiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotiAnnounceDto {
    private Integer id;
    private NotiType type;
    private Boolean isChecked;
    private String message;
    private LocalDateTime createdAt;
}
