package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameUserInfoDto {
    private Integer userId;
    private String userImageUri;
}
