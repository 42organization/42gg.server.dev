package io.pp.arcade.v1.domain.rank.entity;

import io.pp.arcade.v1.domain.rank.dto.RankDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.RacketType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;


@Getter
@RedisHash("user")
@NoArgsConstructor
public class RankRedis implements Serializable {

    @Id
    private Integer id;

    @Indexed /* Redis 조회시 사용되는 변수 */
    private String intraId;

    private RacketType racketType;

    @Indexed
    private GameType gameType;

    @Setter
    private Integer ppp;

    private Integer wins;

    private Integer losses;

    private double winRate;

    @Setter
    private String statusMessage;


    @Builder
    public RankRedis(Integer id, String intraId, Integer ppp, RacketType racketType, GameType gameType, Integer wins, Integer losses, double winRate, String statusMessage) {
        this.id = id;
        this.intraId = intraId;
        this.ppp = ppp;
        this.racketType = racketType;
        this.gameType = gameType;
        this.wins = wins;
        this.losses = losses;
        this.winRate = (wins + losses) == 0 ? 0 : (double)(wins * 10000 / (wins + losses)) / 100;
        this.statusMessage = statusMessage;
    }

    public static RankRedis from (UserDto user, GameType gameType){
        return RankRedis.builder()
                .id(user.getId())
                .intraId(user.getIntraId())
                .ppp(user.getPpp())
                .statusMessage(user.getStatusMessage())
                .gameType(gameType)
                .racketType(user.getRacketType())
                .wins(0)
                .losses(0)
                .winRate(0)
                .build();
    }

    public static RankRedis from (RankDto rankDto, GameType gameType){
        Integer losses = rankDto.getLosses();
        Integer wins = rankDto.getWins();
        RankRedis rankRedis = RankRedis.builder()
                .id(rankDto.getUser().getId())
                .intraId(rankDto.getUser().getIntraId())
                .ppp(rankDto.getPpp())
                .gameType(gameType)
                .racketType(rankDto.getRacketType())
                .wins(rankDto.getWins())
                .losses(rankDto.getLosses())
                .winRate((wins + losses) == 0 ? 0 : (double)(wins * 10000 / (wins + losses)) / 100)
                .statusMessage(rankDto.getStatusMessage())
                .build();
        return rankRedis;
    }

    public void update(Integer isWin, Integer ppp){
        this.ppp = ppp;
        this.wins += isWin;
        this.losses += isWin ^ 1;
        this.winRate = (wins + losses) == 0 ? 0 : (double)(wins * 10000 / (wins + losses)) / 100;
    }

    public void modify(Integer modifyStatus, Integer ppp){
        if (modifyStatus == 0) {
            this.losses--;
            this.wins++;
        }
        else if (modifyStatus == 1) {
            this.losses++;
            this.wins--;
        }
        this.ppp = ppp;
        this.winRate = (wins + losses) == 0 ? 0 : (double)(wins * 10000 / (wins + losses)) / 100;
    }

    public void updateStatusMessage(String statusMessage){
        this.setStatusMessage(statusMessage);
    }
}