package io.pp.arcade.v1.domain.statistic.controller;

import io.pp.arcade.v1.domain.statistic.StatisticService;
import io.pp.arcade.v1.domain.statistic.TableMapper;
import io.pp.arcade.v1.domain.statistic.dto.DataSet;
import io.pp.arcade.v1.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.v1.domain.statistic.dto.FindDataDto;
import io.pp.arcade.v1.domain.statistic.dto.StatisticResponseDto;
import io.pp.arcade.v1.global.type.DateType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pingpong/stat")
public class StatisticControllerImpl implements StatisticController {
    private final StatisticService statisticService;

    /*
     * startAt, endAt null일 경우, 초기화 필요
     * endat 1일 안 보이는 현상 처리 필요
     */
    @GetMapping(value = "/visit/{date}")
    public StatisticResponseDto visit(DateType date, DateRangeDto dateRangeDto) {
        /* datetype에 따라서 일별, 주별, 월별로 일자 변환 -> 리스트에 startat, endat 2개 담기 */
        DateRangeDto changedDateRangeDto = getFirstAndLastDate(date, dateRangeDto);
        FindDataDto findDataDto = FindDataDto.builder()
                .table("visit")
                .dateType(date)
                .startAt(changedDateRangeDto.getFormattedStartAt())
                .endAt(changedDateRangeDto.getFormattedEndAt())
                .build();

        /* service 호출해서 DB에서 변환하면서 데이터 가져오기*/
        List<TableMapper> labelsAndData = statisticService.findDataByCreatedAt(findDataDto);

        /* 데이터 구성 정리하기 */
        List<String> labels = labelsAndData.stream().map(TableMapper::getLabels).collect(Collectors.toList());
        List<Integer> data = labelsAndData.stream().map(TableMapper::getData).collect(Collectors.toList());

        List<DataSet> dataSets = new ArrayList<>();
        dataSets.add(DataSet.builder()
                .label(date.getCode() + "_visit")
                .data(data)
                .build());

        /* 정리한 데이터 responseDto에 넣어주기 */
        StatisticResponseDto responseDto = StatisticResponseDto.builder()
                .labels(labels)
                .datasets(dataSets)
                .build();
        return responseDto;
    }

    @GetMapping(value = "/match/{date}")
    public StatisticResponseDto match(@PathVariable DateType date, @RequestParam DateRangeDto dateRangeDto, HttpServletRequest request) {
        DateRangeDto changedDateRangeDto = getFirstAndLastDate(date, dateRangeDto);
        FindDataDto findDataDto = FindDataDto.builder()
                .table("user")
                .dateType(date)
                .startAt(changedDateRangeDto.getFormattedStartAt())
                .endAt(changedDateRangeDto.getFormattedEndAt())
                .build();
        List<TableMapper> labelsAndDataAllCnt = statisticService.findDataByCreatedAt(findDataDto);
        List<TableMapper> labelsAndDataPureCnt = statisticService.findDataByCreatedAt(findDataDto);


        List<String> labels = labelsAndDataAllCnt.stream().map(TableMapper::getLabels).collect(Collectors.toList());
        List<Integer> dataAllCnt = labelsAndDataAllCnt.stream().map(TableMapper::getData).collect(Collectors.toList());
        List<Integer> dataPureCnt = labelsAndDataPureCnt.stream().map(TableMapper::getData).collect(Collectors.toList());

        List<DataSet> dataSets = new ArrayList<>();
        dataSets.add(DataSet.builder()
                .label("AllCount")
                .data(dataAllCnt)
                .build());
        dataSets.add(DataSet.builder()
                .label("PureCount")
                .data(dataPureCnt)
                .build());

        StatisticResponseDto responseDto = StatisticResponseDto.builder()
                .labels(labels)
                .datasets(dataSets)
                .build();
        return responseDto;
    }

    /* */

    /* */

    DateRangeDto getFirstAndLastDate(DateType dateType, DateRangeDto dateRangeDto) {
        Date startDate = dateRangeDto.getStartat();
        Date endDate = dateRangeDto.getEndat();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setFirstDayOfWeek(Calendar.MONDAY);
        endCal.setFirstDayOfWeek(Calendar.MONDAY);
        startCal.setTime(startDate);
        endCal.setTime(endDate);

        switch (dateType) {
            case WEEKLY:
                getFirstDateOfWeek(startCal);
                getLastDateOfWeek(endCal);
                break;
            case MONTHLY:
                getFirstDateOfMonth(startCal);
                getLastDateOfMonth(endCal);
                break;
        }

        DateRangeDto result = DateRangeDto.builder()
                .startat(startCal.getTime())
                .endat(endCal.getTime())
                .build();
        return result;
    }

    private static void getLastDateOfMonth(Calendar endCal) {
        endCal.set(Calendar.DATE, endCal.getActualMaximum(Calendar.DATE));
    }

    private static void getFirstDateOfMonth(Calendar startCal) {
        startCal.set(Calendar.DAY_OF_MONTH, 1);
    }

    private static void getLastDateOfWeek(Calendar endCal) {
        endCal.set(Calendar.DAY_OF_WEEK, endCal.getFirstDayOfWeek() + 6);
    }

    private static void getFirstDateOfWeek(Calendar startCal) {
        startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }
}
