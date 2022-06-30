package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.type.RacketType;
import lombok.Builder;
import lombok.Getter;

import java.util.Locale;

@Getter
public class UserDetailResponseDto {
    private String intraId;
    private String userImageUri;
    private Integer rank;
    private Integer ppp;
    private Integer wins;
    private Integer losses;
    private Double winRate;
    private String racketType;
    private String statusMessage;
    private UserRivalRecordDto rivalRecord;

    @Builder
    public UserDetailResponseDto(String intraId, String userImageUri, Integer rank, Integer ppp, Integer wins, Integer losses, Double winRate, RacketType racketType, String statusMessage, UserRivalRecordDto rivalRecord) {
        this.intraId = intraId;
        this.userImageUri = userImageUri;
        this.rank = rank;
        this.ppp = ppp;
        this.wins = wins;
        this.losses = losses;
        this.winRate = winRate;
        this.racketType = racketType.getCode();
        this.statusMessage = statusMessage;
        this.rivalRecord = rivalRecord;
    }
}
