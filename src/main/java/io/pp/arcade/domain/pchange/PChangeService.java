package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.admin.dto.create.PChangeCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.PChangeDeleteDto;
import io.pp.arcade.domain.admin.dto.update.PChangeUpdateRequestDto;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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
        Game game = gameRepository.findById(addDto.getGameId()).orElseThrow();
        User user = userRepository.findById(addDto.getUserId()).orElseThrow();
        pChangeRepository.save(PChange.builder()
                .game(game)
                .user(user)
                .pppChange(addDto.getPppChange())
                .pppResult(addDto.getPppResult())
                .build()
        );
    }

    @Transactional
    public List<PChangeDto> findPChangeByGameId(PChangeFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow();
        List<PChange> pChangeList = pChangeRepository.findAllByGame(game).orElseThrow(() -> new IllegalArgumentException("?"));
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public PChangePageDto findPChangeByUserId(PChangeFindDto findDto, Pageable pageable){
        User user = userRepository.findByIntraId(findDto.getIntraId()).orElseThrow();
        Page<PChange> pChangePage = pChangeRepository.findAllByUserOrderByIdDesc(user, pageable);
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
                .totalPage(pChangePage.getTotalPages())
                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangeDto findPChangeByUserAndGame(PChangeFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow();
        User user = userRepository.findByIntraId(findDto.getIntraId()).orElseThrow();
        PChangeDto pChangeDto = PChangeDto.from(pChangeRepository.findByUserAndGame(user, game).orElseThrow());
        return  pChangeDto;
    }

    @Transactional
    public void createPChangeByAdmin(PChangeCreateRequestDto createRequestDto) {
        PChange pChange = PChange.builder()
                .game(gameRepository.findById(createRequestDto.getGameId()).orElseThrow(null))
                .user(userRepository.findByIntraId(createRequestDto.getIntraId()).orElseThrow(null))
                .pppChange(createRequestDto.getPppChange())
                .pppResult(createRequestDto.getPppResult())
                .build();
        pChangeRepository.save(pChange);
    }

    @Transactional
    public void updatePChangeByAdmin(PChangeUpdateRequestDto updateRequestDto) {
        PChange pChange = pChangeRepository.findById(updateRequestDto.getPChangeId()).orElseThrow();
        pChange.setPppChange(updateRequestDto.getPppChange());
        pChange.setPppResult(updateRequestDto.getPppResult());
    }

    @Transactional
    public void deletePChangeByAdmin(PChangeDeleteDto deleteDto) {
        PChange pChange = pChangeRepository.findById(deleteDto.getPChangeId()).orElseThrow();
        pChangeRepository.delete(pChange);
    }

    @Transactional
    public List<PChangeDto> findPChangeByAdmin(Pageable pageable) {
        Page<PChange> pChanges = pChangeRepository.findAll(pageable);
        List<PChangeDto> pChangeDtos = pChanges.stream().map(PChangeDto::from).collect(Collectors.toList());
        return pChangeDtos;
    }
}
