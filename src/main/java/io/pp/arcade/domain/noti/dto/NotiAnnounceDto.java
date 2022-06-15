package io.pp.arcade.domain.noti.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotiAnnounceDto {
    private Integer id;
    private String type;
    private Boolean isChecked;
    private String message;
    private LocalDateTime createdTime;
}
