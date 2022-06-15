package io.pp.arcade.domain.noti.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NotiImminentMatchDto {
    private Integer id;
    private String type;
    private LocalDateTime time;
    private Boolean isChecked;
    private List<String> myTeam;
    private List<String> enemyTeam;
    private LocalDateTime createdTime;
}
