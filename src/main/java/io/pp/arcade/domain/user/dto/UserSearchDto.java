package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserSearchDto {
    String intraId;
}
