package io.pp.arcade.domain.statistic.controller;

import io.pp.arcade.domain.rank.service.RankService;
import io.pp.arcade.domain.statistic.StatisticService;
import io.pp.arcade.domain.statistic.TableMapper;
import io.pp.arcade.domain.statistic.dto.DataSet;
import io.pp.arcade.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.domain.statistic.dto.StatisticResponseDto;
import io.pp.arcade.global.type.DateType;
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
    private final RankService rankService;

    @GetMapping(value = "/visit/{date}")
    public StatisticResponseDto visit(DateType date, DateRangeDto dateRangeDto, HttpServletRequest request) {
        /* datetype에 따라서 일별, 주별, 월별로 일자 변환 -> 리스트에 startat, endat 2개 담기 */
        DateRangeDto changedDataRangeDto = getFirstAndLastDate(date, dateRangeDto);

        /* service 호출해서 DB에서 변환하면서 데이터 가져오기*/
        List<TableMapper> labelsAndData = statisticService.findVisit(date, changedDataRangeDto);

        /* 데이터 구성 정리하기 */
        List<String> labels = labelsAndData.stream().map(TableMapper::getlabels).collect(Collectors.toList());
        List<Integer> data = labelsAndData.stream().map(TableMapper::getdata).collect(Collectors.toList());

        List<DataSet> dataSets = new ArrayList<>();
        dataSets.add(DataSet.builder()
                .label(date.getCode() + "_visit")
                .data(data)
                .build());

        /* 정리한 데이터 responseDto에 넣어주기 */
        StatisticResponseDto responseDto = StatisticResponseDto.builder()
                .labels(labels)
                .dataSets(dataSets)
                .build();
        return responseDto;
    }

    @GetMapping(value = "/visit/{date}/types/rank/section/{section}")
    public StatisticResponseDto visitByRank(@PathVariable DateType date, @PathVariable Integer section, @RequestParam DateRangeDto dateRangeDto, HttpServletRequest request) {
        /* 일자수, 주차수, 월수가 1 넘어가면 안됨 ex) 08-01만, 08.1만, 08만 가능 -> 그걸 검사해야함 */
        DateRangeDto changedDateRangeDto = getFirstAndLastDate(date, dateRangeDto);

        List<TableMapper> labelsAndData = statisticService.findVisit(date, changedDateRangeDto);

        List<String> labels = labelsAndData.stream().map(TableMapper::getlabels).collect(Collectors.toList());
        List<Integer> data = labelsAndData.stream().map(TableMapper::getdata).collect(Collectors.toList());

        List<DataSet> dataSets = new ArrayList<>();
        dataSets.add(DataSet.builder()
                .label("rank" + changedDateRangeDto.getStartat().toString())
                .data(data)
                .build());

        StatisticResponseDto responseDto = StatisticResponseDto.builder()
                .labels(labels)
                .dataSets(dataSets)
                .build();
        return responseDto;
    }

    @GetMapping(value = "/match/{date}")
    public StatisticResponseDto match(@PathVariable DateType date, @RequestParam DateRangeDto dateRangeDto, HttpServletRequest request) {
        DateRangeDto changedDateRangeDto = getFirstAndLastDate(date, dateRangeDto);

        List<TableMapper> labelsAndDataAllCnt = statisticService.findMatch(changedDateRangeDto);
        List<TableMapper> labelsAndDataPureCnt = statisticService.findMatch(changedDateRangeDto);

        List<String> labels = labelsAndDataAllCnt.stream().map(TableMapper::getlabels).collect(Collectors.toList());
        List<Integer> dataAllCnt = labelsAndDataAllCnt.stream().map(TableMapper::getdata).collect(Collectors.toList());
        List<Integer> dataPureCnt = labelsAndDataPureCnt.stream().map(TableMapper::getdata).collect(Collectors.toList());

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
                .dataSets(dataSets)
                .build();
        return responseDto;
    }

    @GetMapping(value = "/match/{date}/types/rank/section/{section}")
    public StatisticResponseDto matchByRank(@PathVariable DateType date, @PathVariable Integer section, @RequestParam DateRangeDto dateRangeDto, HttpServletRequest request) {
        DateRangeDto changedDateRangeDto = getFirstAndLastDate(date, dateRangeDto);

        List<TableMapper> labelsAndDataAllCnt = statisticService.findMatch(changedDateRangeDto);
        List<TableMapper> labelsAndDataPureCnt = statisticService.findMatch(changedDateRangeDto);

        List<String> labels = labelsAndDataAllCnt.stream().map(TableMapper::getlabels).collect(Collectors.toList());
        List<Integer> dataAllCnt = labelsAndDataAllCnt.stream().map(TableMapper::getdata).collect(Collectors.toList());
        List<Integer> dataPureCnt = labelsAndDataPureCnt.stream().map(TableMapper::getdata).collect(Collectors.toList());

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
                .dataSets(dataSets)
                .build();
        return responseDto;
    }

    /*
    @GetMapping(value = "/match/cancel/{date}")
    StatisticResponseDto matchCancel() {

    }

    @GetMapping(value = "/match/{date}/types/generation/{generation}")
    StatisticResponseDto matchByGeneration() {

    }

    @GetMapping(value = "/slotin/{date}")
    StatisticResponseDto matchBySlotinTime() {

    }

    @GetMapping(value = "/slottime/{date}")
    StatisticResponseDto matchBySlotTime() {

    }
    */

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

        dateRangeDto.setEndat(startCal.getTime());
        dateRangeDto.setStartat(endCal.getTime());
        return dateRangeDto;
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