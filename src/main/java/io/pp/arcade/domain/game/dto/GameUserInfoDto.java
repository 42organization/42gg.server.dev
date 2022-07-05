package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GameUserInfoDto {
    private String intraId;
    private String userImageUri;
}
