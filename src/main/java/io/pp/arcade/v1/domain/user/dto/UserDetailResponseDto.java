package io.pp.arcade.v1.domain.user.dto;

import io.pp.arcade.v1.global.type.RacketType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailResponseDto {
    private String intraId;
    private String userImageUri;
    private String racketType;
    private String statusMessage;
    private Integer level;
    private Integer currentExp;
    private Integer maxExp;
    private Double expRate;
    private UserRivalRecordDto rivalRecord;

    @Builder
    public UserDetailResponseDto(String intraId, String userImageUri, Integer level, Integer currentExp, Integer maxExp, RacketType racketType, String statusMessage, UserRivalRecordDto rivalRecord) {
        this.intraId = intraId;
        this.userImageUri = userImageUri;
        this.racketType = racketType.getCode();
        this.statusMessage = statusMessage;
        this.level = level;
        this.currentExp = currentExp;
        this.maxExp = maxExp;
        this.expRate = (double)(currentExp * 10000 / maxExp) / 100;
        this.rivalRecord = rivalRecord;
    }

    @Override
    public String toString() {
        return "UserDetailResponseDto{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", racketType='" + racketType + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", level=" + level +
                ", currentExp=" + currentExp +
                ", maxExp=" + maxExp +
                ", rivalRecord=" + rivalRecord +
                '}';
    }
}
