package io.pp.arcade.domain.statistic.dto;

import io.pp.arcade.global.type.DateType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindDataDto {
    String startAt;
    String endAt;
    String table;
    DateType dateType;

    @Override
    public String toString() {
        return "FindDataDto{" +
                "startAt='" + startAt + '\'' +
                ", endAt='" + endAt + '\'' +
                ", table='" + table + '\'' +
                ", dateType=" + dateType +
                '}';
    }
}
