package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserRivalRecordDto {
    Integer curUserWin;
    Integer targetUserWin;
}
