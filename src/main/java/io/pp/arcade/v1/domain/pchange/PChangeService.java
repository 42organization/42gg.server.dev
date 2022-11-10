package io.pp.arcade.v1.domain.pchange;

import io.pp.arcade.v1.domain.admin.dto.create.PChangeCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.delete.PChangeDeleteDto;
import io.pp.arcade.v1.domain.admin.dto.update.PChangeUpdateDto;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.pchange.dto.*;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.util.ExpLevelCalculator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PChangeService {
    private final PChangeRepository pChangeRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addPChange(PChangeAddDto addDto) {
        Game game = gameRepository.findById(addDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));
        User user = userRepository.findById(addDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));

        pChangeRepository.save(PChange.builder()
                .game(game)
                .user(user)
                .pppChange(addDto.getPppChange() != null ? addDto.getPppChange() : 0)
                .pppResult(addDto.getPppResult() != null ? addDto.getPppResult() : 0)
                .expChange(addDto.getExpChange())
                .expResult(addDto.getExpResult())
                .build()
        );
    }

    @Transactional
    public List<PChangeDto> findPChangeByUserIdNotPage(PChangeFindDto findDto){
        List<PChange> pChangeList = pChangeRepository.findAllByUserIntraId(findDto.getUser().getIntraId()).orElseThrow(() -> new BusinessException("E0001"));;
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public PChangeListDto findRankPChangeByUserId(PChangeFindDto findDto){
        List<PChange> pChangePage = pChangeRepository.findPChangeHistory(findDto.getUser().getIntraId(), findDto.getSeason(), Mode.RANK.getValue(), findDto.getCount());
        Collections.reverse(pChangePage);
        PChangeListDto dto = PChangeListDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
                .build();
        return dto;
    }

    @Transactional
    public PChangeListDto findPChangeByUserIdAfterGameIdAndGameMode(PChangeListFindDto findDto){
        Integer gameId = findDto.getGameId() == null ? Integer.MAX_VALUE : findDto.getGameId();
        Integer mode = findDto.getMode() == null ? null : findDto.getMode().getValue();
        User user = userRepository.findByIntraId(findDto.getIntraId()).orElseThrow(() -> new BusinessException("UF001"));

        List<PChange> pChangePage = pChangeRepository.findPChangesByGameModeAndUser(findDto.getSeason(), mode, user.getIntraId(), gameId, findDto.getCount() + 1);
        Integer count = pChangePage.size();
        if (count == findDto.getCount() + 1)
            pChangePage.remove(count - 1);
        PChangeListDto dto = PChangeListDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
                .isLast(count < findDto.getCount() + 1)
                .build();
        return dto;
    }

    @Transactional
    public PChangeDto findPChangeByUserAndGame(PChangeFindDto findDto) {
        PChange pChange = pChangeRepository.findByUser_IntraIdAndGame_Id(findDto.getUser().getIntraId(), findDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));

        PChangeDto pChangeDto = PChangeDto.from(pChange);
        return  pChangeDto;
    }

    @Transactional
    public GameExpAndPppResultDto findPChangeExpByUserAndGame(PChangeFindDto findDto) {
        PChange pChange = pChangeRepository.findByUser_IntraIdAndGame_Id(findDto.getUser().getIntraId(), findDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer beforeTotalExp = pChange.getExpResult() - pChange.getExpChange();
        Integer beforeLevel = ExpLevelCalculator.getLevel(beforeTotalExp);
        Integer beforeMaxExp = ExpLevelCalculator.getLevelMaxExp(ExpLevelCalculator.getLevel(beforeTotalExp));
        Integer increasedExp = pChange.getExpChange();
        Integer afterMaxExp = ExpLevelCalculator.getLevelMaxExp(ExpLevelCalculator.getLevel(pChange.getExpResult()));
        Integer increasedLevel = ExpLevelCalculator.getLevel(pChange.getExpResult()) - beforeLevel;

        GameExpAndPppResultDto resultDto = GameExpAndPppResultDto.builder()
                .beforeExp(ExpLevelCalculator.getCurrentLevelMyExp(beforeTotalExp))
                .increasedExp(increasedExp)
                .beforeMaxExp(beforeMaxExp)
                .beforeLevel(beforeLevel)
                .increasedLevel(increasedLevel)
                .afterMaxExp(afterMaxExp)
                .pppChange(pChange.getPppChange())
                .pppResult(pChange.getPppResult())
                .build();
        return resultDto;
    }

    @Transactional
    public Integer findPChangeIdByUserAndGame(PChangeFindDto findDto) {
        PChange pChange = pChangeRepository.findByUser_IntraIdAndGame_Id(findDto.getUser().getIntraId(), findDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));
        return  pChange.getId();
    }

    @Transactional
    public void createPChangeByAdmin(PChangeCreateRequestDto createRequestDto) {
        PChange pChange = PChange.builder()
                .game(gameRepository.findById(createRequestDto.getGameId()).orElseThrow(null))
                .user(userRepository.findById(createRequestDto.getUserId()).orElseThrow(null))
                .pppChange(createRequestDto.getPppChange())
                .pppResult(createRequestDto.getPppResult())
                .build();
        pChangeRepository.save(pChange);
    }

    @Transactional
    public void updatePChangeByAdmin(Integer pChangeId, PChangeUpdateDto updateDto) {
        PChange pChange = pChangeRepository.findById(pChangeId).orElseThrow();
        pChange.setPppChange(updateDto.getPppChange());
        pChange.setPppResult(updateDto.getPppResult());
    }

    @Transactional
    public void deletePChangeByAdmin(PChangeDeleteDto deleteDto) {
        PChange pChange = pChangeRepository.findById(deleteDto.getPChangeId()).orElseThrow();
        pChangeRepository.delete(pChange);
    }

    @Transactional
    public List<PChangeDto> findPChangeByAdmin(Pageable pageable) {
        Page<PChange> pChanges = pChangeRepository.findAllByOrderByIdDesc(pageable);
        List<PChangeDto> pChangeDtos = pChanges.stream().map(PChangeDto::from).collect(Collectors.toList());
        return pChangeDtos;
    }
}
