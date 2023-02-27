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


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        List<PChange> pChangeList = pChangeAdminRepository.findPChangesByUserIntraId(intraId, pageable);  //해당 유저의 모든 pChange 가져오기
        System.out.println("===============");
        System.out.println("pChange개수 =" + pChangeList.size());
        System.out.println("===============");
        List<Game> games = pChangeList.stream().map(PChange::getGame).collect(Collectors.toList());   //pChange에서 Game 정보 가져오기

        return createGameLogAdminDto(games, pageable);
    }

    @Transactional
    public GameLogListAdminResponseDto createGameLogAdminDto(List<Game> games, Pageable pageable){
        int gameNum = games.size();
        List<GameLogAdminDto> gameLogAdminDtoList = new ArrayList<>();

        for(int i=0;i<gameNum;i++){     // -> GameLogAdminDto 1개씩 생성
            Game game = games.get(i);    //해당 게임에 대해 정보 찾기
            Optional<Slot> slot = slotAdminRepository.findById(game.getSlot().getId());      //해당 게임의 슬롯id로 슬롯 찾기
            //List<SlotTeamUser> stuList = slotTeamUserAdminRepository.findAllBySlot(slot);  // 2개(단식) or 4개(복식)
            SlotTeamUser stu1 = slotTeamUserAdminRepository.findAllBySlot(slot).get(0);
            SlotTeamUser stu2 = slotTeamUserAdminRepository.findAllBySlot(slot).get(1);
            //여기를 반복문으로 처리하고 함수로 따로 빼서 gameTeamAdminDto를 여러개 받아오는 방식으로 수정할 수 있지 않을까

            GameTeamAdminDto team1 = GameTeamAdminDto.builder()
                    .intraId1(stu1.getUser().getIntraId())
                    .teamId(stu1.getTeam().getId())
                    .score(stu1.getTeam().getScore())
                    .win(stu1.getTeam().getWin())
                    .build();

            GameTeamAdminDto team2 = GameTeamAdminDto.builder()
                    .intraId1(stu2.getUser().getIntraId())
                    .teamId(stu2.getTeam().getId())
                    .score(stu2.getTeam().getScore())
                    .win(stu2.getTeam().getWin())
                    .build();

            GameLogAdminDto gameLog = GameLogAdminDto.builder()
                    .gameId(game.getId())
                    .startAt(slot.get().getTime())
                    .mode(game.getMode().getValue() == 1? "Normal" : "Rank")
                    .team1(team1)
                    .team2(team2)
                    .build();

            gameLogAdminDtoList.add(gameLog);
        }

        gameLogAdminDtoList = gameLogAdminDtoList.stream().sorted(Comparator.comparing(GameLogAdminDto::getGameId).reversed()).collect(Collectors.toList());
        /*list -> page*/
        //Pageable pageable = PageRequest.of(page - 1, size, Sort.by("game_id").descending());
        Page<GameLogAdminDto> gameLogAdminDtoPage = new PageImpl<>(gameLogAdminDtoList, pageable, gameLogAdminDtoList.size());
        GameLogListAdminResponseDto responseDto = new GameLogListAdminResponseDto(gameLogAdminDtoPage.getContent(),
                gameLogAdminDtoPage.getTotalPages(), gameLogAdminDtoPage.getNumber() + 1);
        return responseDto;
    }
}
