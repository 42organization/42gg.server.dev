package io.pp.arcade.v1.admin.game.service;

import io.pp.arcade.v1.admin.game.dto.GameLogAdminDto;
import io.pp.arcade.v1.admin.game.dto.GameLogListAdminResponseDto;
import io.pp.arcade.v1.admin.game.dto.GameTeamAdminDto;
import io.pp.arcade.v1.admin.game.repository.GameAdminRepository;
import io.pp.arcade.v1.admin.pchange.repository.PChangeAdminRepository;
import io.pp.arcade.v1.admin.slot.repository.SlotAdminRepository;
import io.pp.arcade.v1.admin.slotteamuser.repository.SlotTeamUserAdminRepository;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.pchange.PChange;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameAdminService {
    private final GameAdminRepository gameAdminRepository;
    private final SlotAdminRepository slotAdminRepository;
    private final SlotTeamUserAdminRepository slotTeamUserAdminRepository;
    private final PChangeAdminRepository pChangeAdminRepository;

    @Transactional
    public GameLogListAdminResponseDto findAllGamesByAdmin(Pageable pageable){
        List<Game> games = gameAdminRepository.findAll();  //모든 게임 정보 가져오기
        return createGameLogAdminDto(games, pageable);
    }

    @Transactional
    public GameLogListAdminResponseDto findGamesBySeasonId(int seasonId, Pageable pageable){
        List<Game> games = gameAdminRepository.findBySeason(seasonId);   //시즌 id로 게임들 찾아오기
        return createGameLogAdminDto(games, pageable);
    }

    @Transactional
    public GameLogListAdminResponseDto findGamesByIntraId(String intraId, Pageable pageable){
        List<PChange> pChangeList = pChangeAdminRepository.findPChangesByUser_IntraId(intraId);  //해당 유저의 모든 pChange 가져오기
        List<Game> games = pChangeList.stream().map(PChange::getGame).collect(Collectors.toList());   //pChange에서 Game 정보 가져오기
        return createGameLogAdminDto(games, pageable);
    }

    @Transactional
    public GameLogListAdminResponseDto createGameLogAdminDto(List<Game> games, Pageable pageable){
        int gameNum = games.size();

        //games에서 요청한 페이지에 해당하는 데이터가져오기
        int subStart = Math.min(Math.max(pageable.getPageSize() * (pageable.getPageNumber()), 0), games.size());
        int subEnd = Math.min((subStart + pageable.getPageSize()), games.size());
        List<Game> gameSubList = games.subList(subStart, subEnd);
        int subNum = gameSubList.size();

        List<GameLogAdminDto> gameLogAdminDtoList = new ArrayList<>();

        for(int i=0;i<subNum;i++){     // -> GameLogAdminDto 1개씩 생성
            Game game = gameSubList.get(i);    //해당 게임에 대해 정보 찾기
            Optional<Slot> slot = slotAdminRepository.findById(game.getSlot().getId());      //해당 게임의 슬롯id로 슬롯 찾기
            List<SlotTeamUser> stuList = slotTeamUserAdminRepository.findAllBySlot(slot);  // 2개(단식) or 4개(복식)
            //stuList = stuList.stream().sorted(Comparator.comparing(SlotTeamUser::getTeam)).collect(Collectors.toList());
            Collections.sort(stuList, (a,b) -> a.getTeam().getId() - b.getTeam().getId());

            SlotTeamUser stu1, stu2 = null, stu3, stu4 = null;
            if (stuList.size() == 2){   //단식
                stu1 = stuList.get(0);  //1팀
                stu3 = stuList.get(1);  //2팀
            }else{  //복식
                stu1 = stuList.get(0);  //1팀
                stu2 = stuList.get(1);  //1팀
                stu3 = stuList.get(2);  //2팀
                stu4 = stuList.get(3);  //2팀
            }

            GameTeamAdminDto team1 = GameTeamAdminDto.builder()
                    .intraId1(stu1.getUser().getIntraId())
                    .intraId2(stu2 == null ? null : stu2.getUser().getIntraId())
                    .teamId(stu1.getTeam().getId())
                    .score(stu1.getTeam().getScore())
                    .win(stu1.getTeam().getWin())
                    .build();

            GameTeamAdminDto team2 = GameTeamAdminDto.builder()
                    .intraId1(stu3.getUser().getIntraId())
                    .intraId2(stu4 == null ? null : stu4.getUser().getIntraId())
                    .teamId(stu3.getTeam().getId())
                    .score(stu3.getTeam().getScore())
                    .win(stu3.getTeam().getWin())
                    .build();

            GameLogAdminDto gameLog = GameLogAdminDto.builder()
                    .gameId(game.getId())
                    .startAt(slot.get().getTime())
                    .playTime(slot.get().getEndTime() == null ? null : String.valueOf(Duration.between(slot.get().getTime().toLocalTime(), slot.get().getEndTime().toLocalTime()).toMinutes()))
                    .mode(game.getMode().getValue() == 1? "Normal" : "Rank")
                    .team1(team1)
                    .team2(team2)
                    .build();

            gameLogAdminDtoList.add(gameLog);
        }

        /*list -> page*/
        int totalStart = Math.min(Math.max(pageable.getPageSize() * (pageable.getPageNumber()), 0), gameNum);
        int totalEnd = Math.min((totalStart + pageable.getPageSize()), gameNum);
        Page<Game> totalgamePage = new PageImpl<>(games.subList(totalStart, totalEnd), pageable, gameNum);
        Page<GameLogAdminDto> gameLogAdminDtoPage = new PageImpl<>(gameLogAdminDtoList, pageable, gameLogAdminDtoList.size());
        GameLogListAdminResponseDto responseDto = new GameLogListAdminResponseDto(gameLogAdminDtoPage.getContent(),
                totalgamePage.getTotalPages(), totalgamePage.getNumber() + 1);
        return responseDto;
    }
}
