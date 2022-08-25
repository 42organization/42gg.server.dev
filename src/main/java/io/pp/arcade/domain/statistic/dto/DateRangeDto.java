package io.pp.arcade.domain.statistic.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class DateRangeDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date startat;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date endat;
}
