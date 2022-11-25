package io.pp.arcade.v1.domain.opponent.dto;

import io.pp.arcade.v1.domain.opponent.Opponent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OpponentResponseDto {

    private String intraId;
    private String nick;
    private String imageUrl;
    private String detail;
    private Boolean isReady;

    @Builder
    public OpponentResponseDto(Opponent opponent) {
        this.intraId = opponent.getIntraId();
        this.nick = opponent.getNick();
        this.imageUrl = opponent.getImageUrl();
        this.detail = opponent.getDetail();
        this.isReady = opponent.getIsReady();
    }

    public static OpponentResponseDto from(Opponent opponent) {
        return new OpponentResponseDto(opponent);
    }
}
