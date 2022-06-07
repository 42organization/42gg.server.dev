package io.gg.arcade.domain.slot.dto;

import io.gg.arcade.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@Builder
public class SlotFindDto {
    private LocalDateTime localDateTime;
    private Integer currentUserPpp;
    private Integer userId;
    private String inquiringType;
}
