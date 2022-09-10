package io.pp.arcade.v1.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRivalRecordDto {
    Integer curUserWin;
    Integer targetUserWin;

    @Override
    public String toString() {
        return "UserRivalRecordDto{" +
                "curUserWin=" + curUserWin +
                ", targetUserWin=" + targetUserWin +
                '}';
    }
}
