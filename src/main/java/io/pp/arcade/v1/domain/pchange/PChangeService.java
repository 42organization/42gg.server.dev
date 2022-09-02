package io.pp.arcade.v1.domain.pchange;

import io.pp.arcade.v1.domain.admin.dto.create.PChangeCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.delete.PChangeDeleteDto;
import io.pp.arcade.v1.domain.admin.dto.update.PChangeUpdateDto;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.exception.BusinessException;
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
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow(() -> new BusinessException("E0001"));
        List<PChange> pChangeList = pChangeRepository.findAllByGame(game).orElseThrow(() -> new BusinessException("E0001"));
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public List<PChangeDto> findPChangeByUserIdNotPage(PChangeFindDto findDto){
        List<PChange> pChangeList = pChangeRepository.findAllByUserIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));;
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public PChangePageDto findPChangeByUserId(PChangeFindDto findDto){
        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        Page<PChange> pChangePage = pChangeRepository.findAllByUserOrderByIdDesc(user, findDto.getPageable());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()).stream().sorted(Comparator.comparing(PChangeDto::getId)).collect(Collectors.toList()))
//                .totalPage(pChangePage.getTotalPages())
//                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangePageDto findPChangeByUserIdAfterGameId(PChangeFindDto findDto){
        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer gameId = findDto.getGameId() == null ? Integer.MAX_VALUE : findDto.getGameId();

        Page<PChange> pChangePage = pChangeRepository.findAllByUserAndGameIdLessThanOrderByIdDesc(user, gameId, findDto.getPageable());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
                .totalPage(pChangePage.getTotalPages())
                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangePageDto findPChangeByUserIdAfterGameIdAndGameMode(PChangeFindDto findDto){
//        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer gameId = findDto.getGameId() == null ? Integer.MAX_VALUE : findDto.getGameId();
        Integer mode = findDto.getMode().getValue();

        Page<PChange> pChangePage = pChangeRepository.findPChangesByGameModeAndUser(mode, findDto.getUserId(), gameId, findDto.getPageable());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
                .totalPage(pChangePage.getTotalPages())
                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangeDto findPChangeByUserAndGame(PChangeFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow(() -> new BusinessException("E0001"));
        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        PChange pChange = pChangeRepository.findByUserAndGame(user, game).orElse(PChange.builder().game(game).user(user).pppChange(0).pppResult(user.getPpp()).build());

        PChangeDto pChangeDto = PChangeDto.from(pChange);
        return  pChangeDto;
    }

    @Transactional
    public Integer findPChangeIdByUserAndGame(PChangeFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow(() -> new BusinessException("E0001"));
        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        PChange pChange = pChangeRepository.findByUserAndGame(user, game).orElseThrow(() -> new BusinessException("E0001"));
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
