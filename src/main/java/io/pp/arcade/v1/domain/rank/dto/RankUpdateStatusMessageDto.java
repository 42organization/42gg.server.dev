package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankUpdateStatusMessageDto {
    private SeasonDto seasonDto;
    private UserDto userDto;
    private String statusMessage;

    @Override
    public String toString() {
        return "RankModifyStatusMessageDto{" +
                "intraId='" + userDto.getIntraId() + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}
