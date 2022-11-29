package io.pp.arcade.v1.domain.rank.service;

import io.pp.arcade.RealWorld;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.domain.rank.entity.Rank;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.RacketType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class RankServiceTest {

    @Autowired
    private RealWorld realWorld;
    @Autowired
    private RankRepository rankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankService rankService;
    @Autowired
    private TestInitiator testInitiator;

    private User[] users;
    private List<Rank> ranks;
    private Season season;

    @BeforeEach
    void init()
    {
        testInitiator.letsgo();
        users = testInitiator.users;
        season = testInitiator.testSeason;

        ranks = new ArrayList<>();
        List<User> userList = Arrays.stream(users).collect(Collectors.toList());
        for (User user : userList) {
            Rank singleRank = Rank.builder().user(user).ppp(user.getPpp()).seasonId(season.getId()).racketType(RacketType.NONE).losses(0).wins(0).statusMessage(user.getIntraId()).gameType(GameType.SINGLE).ranking(0).build();
            ranks.add(singleRank);
        }
        rankRepository.saveAll(ranks);
    }

    @Test
    @Transactional
    @DisplayName("랭킹 조회")
    void findRankingById() {
        // given
        User user = users[0];
        RankRankingFindDto rankingFindDto = RankRankingFindDto.builder().seasonDto(SeasonDto.from(season)).intraId(user.getIntraId()).build();

        // when
        Integer ranking = rankService.findRankingById(rankingFindDto);

        // then
        Assertions.assertThat(ranking).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("랭크 리스트 조회")
    void findRankList() {
        // given
        Integer count = 3;
        Integer page = 1;
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "ppp"));
        RankFindListDto rankingFindDto = RankFindListDto.builder().seasonId(season.getId()).count(count).gameType(GameType.SINGLE).pageable(pageable).build();

        // when
        RankListDto rankListDto = rankService.findRankList(rankingFindDto);

        // then
        Assertions.assertThat(rankListDto).isNotNull();
        Assertions.assertThat(rankListDto.getCurrentPage()).isEqualTo(page);
        Integer totalPage = (ranks.size() / count) == 0 ? 1 : (ranks.size() / count);
        Assertions.assertThat(rankListDto.getTotalPage()).isEqualTo(totalPage);
    }

    @Test
    @Transactional
    @DisplayName("유저 추가시 랭크 추가")
    void addUserRank() {
        //given
        Integer prev_ranks_size = rankRepository.findAll().size();
        User user = realWorld.getUserWithOutRank();

        //when
        rankService.addUserRank(UserDto.from(user));

        //then
        Integer cur_ranks_size = rankRepository.findAll().size();
        Assertions.assertThat(cur_ranks_size).isEqualTo(prev_ranks_size + 1);
    }

    @Test
    @Transactional
    @DisplayName("Ppp 업데이트")
    void updateRankPpp() {
        //given
        UserDto userDto = UserDto.from(users[0]);
        SeasonDto seasonDto = SeasonDto.from(season);

        Rank prevUserRank = rankRepository.findBySeasonIdAndUserId(seasonDto.getId(), userDto.getId()).get();
        Integer prevWins = prevUserRank.getWins();
        Integer prevLosses = prevUserRank.getLosses();
        Integer updatePpp = 100;
        Boolean isWin = true;

        RankUpdateDto rankUpdateDto =  RankUpdateDto.builder()
            .gameType(GameType.SINGLE)
            .updatePpp(updatePpp)
            .userDto(userDto)
            .isWin(isWin)
            .seasonDto(seasonDto)
            .build();


        //when
        rankService.updateRankPpp(rankUpdateDto);

        //then
        Rank curUserRank = rankRepository.findBySeasonIdAndUserId(seasonDto.getId(), userDto.getId()).get();
        Assertions.assertThat(curUserRank).isNotNull();
        Assertions.assertThat(curUserRank.getPpp()).isEqualTo(updatePpp);
        Assertions.assertThat(curUserRank.getWins()).isEqualTo(prevWins + (isWin ? 1 : 0));
        Assertions.assertThat(curUserRank.getLosses()).isEqualTo(prevLosses + (!isWin ? 1 : 0));
    }

    @Test
    @Transactional
    @DisplayName("StatusMessage 업데이트")
    void updateRankStatusMessage() {
        //given
        UserDto userDto = UserDto.from(users[0]);
        SeasonDto seasonDto = SeasonDto.from(season);

        String updateStatusMessage = "update";
        RankUpdateStatusMessageDto updateDto = RankUpdateStatusMessageDto.builder().userDto(userDto).seasonDto(seasonDto).statusMessage(updateStatusMessage).build();

        //when
        rankService.updateRankStatusMessage(updateDto);

        //then
        Rank curUserRank = rankRepository.findBySeasonIdAndUserId(seasonDto.getId(), userDto.getId()).get();
        Assertions.assertThat(curUserRank.getStatusMessage()).isEqualTo(updateStatusMessage);
    }

    @Test
    @Transactional
    @DisplayName("유저 랭크 조회")
    void findRankById() {
        //given
        UserDto userDto = UserDto.from(users[0]);
        SeasonDto seasonDto = SeasonDto.from(season);

        RankRedisFindDto findDto = RankRedisFindDto.builder().user(userDto).seasonDto(seasonDto).gameType(GameType.SINGLE).build();
        //when
        RankUserDto rankUserDto = rankService.findRank(findDto);

        //then
        Assertions.assertThat(rankUserDto).isNotNull();
        Assertions.assertThat(rankUserDto.getIntraId()).isEqualTo(userDto.getIntraId());
    }
}