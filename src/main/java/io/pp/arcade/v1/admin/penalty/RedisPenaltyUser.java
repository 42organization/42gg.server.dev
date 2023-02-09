package io.pp.arcade.v1.admin.penalty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("penalty")
@Getter
@NoArgsConstructor
public class RedisPenaltyUser {

    @Id
    private String id;
    private String intraId;
    private int penaltyTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime releaseTime;
    private String reason;

    public RedisPenaltyUser(String intraId, int penaltyTime, LocalDateTime releaseTime, String reason) {
        this.intraId = intraId;
        this.penaltyTime = penaltyTime;
        this.releaseTime = releaseTime;
        this.reason = reason;
    }
}
