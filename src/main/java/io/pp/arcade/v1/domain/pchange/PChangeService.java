package io.pp.arcade.v1.domain.pchange;

import io.pp.arcade.v1.domain.admin.dto.create.PChangeCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.delete.PChangeDeleteDto;
import io.pp.arcade.v1.domain.admin.dto.update.PChangeUpdateDto;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.game.dto.GameExpResultResponseDto;
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
import java.util.Comparator;
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
        Game game = gameRepository.findById(addDto.getGameId()).orElseThrow(() -> new BusinessException("E0001"));
        User user = userRepository.findById(addDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));

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
    public List<PChangeDto> findPChangeByGameId(PChangeFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));
        List<PChange> pChangeList = pChangeRepository.findAllByGame(game).orElseThrow(() -> new BusinessException("E0001"));
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public List<PChangeDto> findPChangeByUserIdNotPage(PChangeFindDto findDto){
        List<PChange> pChangeList = pChangeRepository.findAllByUserIntraId(findDto.getUser().getIntraId()).orElseThrow(() -> new BusinessException("E0001"));;
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public PChangePageDto findRankPChangeByUserId(PChangeFindDto findDto){
//        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        Page<PChange> pChangePage = pChangeRepository.findPChangeHistory(findDto.getUser().getIntraId(), findDto.getSeason(), Mode.RANK.getValue(), findDto.getPageable());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()).stream().sorted(Comparator.comparing(PChangeDto::getId)).collect(Collectors.toList()))
//                .totalPage(pChangePage.getTotalPages())
//                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangePageDto findPChangeByUserIdAfterGameId(PChangeFindDto findDto){
        User user = userRepository.findByIntraId(findDto.getUser().getIntraId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer gameId = findDto.getGame() == null ? Integer.MAX_VALUE : findDto.getGame().getId();

        Page<PChange> pChangePage = pChangeRepository.findAllByUserAndGameIdLessThanOrderByIdDesc(user, gameId, findDto.getPageable());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
                .totalPage(pChangePage.getTotalPages())
                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangePageDto findPChangeByUserIdAfterGameIdAndGameMode(PChangeListFindDto findDto){
//        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer gameId = findDto.getGameId() == null ? Integer.MAX_VALUE : findDto.getGameId();
        Integer mode = findDto.getMode() == null ? null : findDto.getMode().getValue();

        List<PChange> pChangePage = pChangeRepository.findPChangesByGameModeAndUser(findDto.getSeason(), mode, findDto.getIntraId(), gameId, findDto.getCount());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
                .build();
        return dto;
    }

    @Transactional
    public PChangeDto findPChangeByUserAndGame(PChangeFindDto findDto) {
//        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow(() -> new BusinessException("E0001"));
//        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        PChange pChange = pChangeRepository.findByUser_IntraIdAndGame_Id(findDto.getUser().getIntraId(), findDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));

        PChangeDto pChangeDto = PChangeDto.from(pChange);
        return  pChangeDto;
    }

    @Transactional
    public GameExpResultResponseDto findPChangeExpByUserAndGame(PChangeFindDto findDto) {
        PChange pChange = pChangeRepository.findByUser_IntraIdAndGame_Id(findDto.getUser().getIntraId(), findDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer beforeTotalExp = pChange.getExpResult() - pChange.getExpChange();
        Integer increasedExp = pChange.getExpChange();
        Integer beforeMaxExp = ExpLevelCalculator.getLevelMaxExp(ExpLevelCalculator.getLevel(beforeTotalExp));
        Integer afterMaxExp = ExpLevelCalculator.getLevelMaxExp(ExpLevelCalculator.getLevel(pChange.getExpResult()));
        Integer increasedLevel = ExpLevelCalculator.getLevel(pChange.getExpResult()) - ExpLevelCalculator.getLevel(beforeTotalExp);

        GameExpResultResponseDto responseDto = GameExpResultResponseDto.builder()
                .beforeExp(ExpLevelCalculator.getCurrentLevelMyExp(beforeTotalExp))
                .increasedExp(increasedExp)
                .beforeMaxExp(beforeMaxExp)
                .beforeLevel(ExpLevelCalculator.getLevel(beforeTotalExp))
                .increasedLevel(ExpLevelCalculator.getLevel(increasedLevel))
                .afterMaxExp(afterMaxExp)
                .build();
        return responseDto;
    }

    @Transactional
    public Integer findPChangeIdByUserAndGame(PChangeFindDto findDto) {
//        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow(() -> new BusinessException("E0001"));
//        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
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
