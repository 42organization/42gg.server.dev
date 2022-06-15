//package io.pp.arcade.domain.rank.controller;
//
//import io.pp.arcade.domain.rank.Rank;
//import io.pp.arcade.domain.rank.RankService;
//import io.pp.arcade.domain.rank.dto.FindRankDto;
//import io.pp.arcade.domain.rank.dto.RankListResponseDto;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@AllArgsConstructor
//public class RankControllerImpl implements RankController {
//    private final RankService service;
//
//    @Override
//    public RankListResponseDto rankList(Pageable pageable, Integer userId) {
//        List<FindRankDto> findListDto = service.findRankList(pageable);
//        RankListResponseDto.builder()
//                .
//                .build();
//        return null;
//    }
//}
