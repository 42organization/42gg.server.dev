package io.gg.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAddRequestDto {
    String intraId;
    String userImgUri;
}
