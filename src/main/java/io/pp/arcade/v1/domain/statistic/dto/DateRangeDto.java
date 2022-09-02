package io.pp.arcade.v1.domain.statistic.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Builder
public class DateRangeDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startat;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endat;

    public String getFormattedStartAt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(startat);
    }

    public String getFormattedEndAt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(endat);
    }
}
