package io.pp.arcade.v1.domain.statistic.dto;

import io.pp.arcade.v1.global.type.DateType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindDataDto {
    String startAt;
    String endAt;
    String table;
    String query;
    DateType dateType;

    @Override
    public String toString() {
        return "FindDataDto{" +
                "startAt='" + startAt + '\'' +
                ", endAt='" + endAt + '\'' +
                ", table='" + table + '\'' +
                ", query='" + query + '\'' +
                ", dateType=" + dateType +
                '}';
    }
}
