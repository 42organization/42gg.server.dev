package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        Game game = gameRepository.findById(addDto.getGameId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        User user = userRepository.findById(addDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
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
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        List<PChange> pChangeList = pChangeRepository.findAllByGame(game).orElseThrow(() -> new BusinessException("{invalid.request}"));
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public PChangePageDto findPChangeByUserId(PChangeFindDto findDto){
        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        Page<PChange> pChangePage = pChangeRepository.findAllByUserOrderByIdDesc(user, findDto.getPageable());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
//                .totalPage(pChangePage.getTotalPages())
//                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangePageDto findPChangeByUserIdAfterGameId(PChangeFindDto findDto){
        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        Integer gameId = findDto.getGameId() == null ? Integer.MAX_VALUE : findDto.getGameId();

        Page<PChange> pChangePage = pChangeRepository.findAllByUserAndGameIdLessThanOrderByIdDesc(user, gameId, findDto.getPageable());
        PChangePageDto dto = PChangePageDto.builder()
                .pChangeList(pChangePage.stream().map(PChangeDto::from).collect(Collectors.toList()))
//                .totalPage(pChangePage.getTotalPages())
//                .currentPage(pChangePage.getPageable().getPageNumber())
                .build();
        return dto;
    }

    @Transactional
    public PChangeDto findPChangeByUserAndGame(PChangeFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        User user = userRepository.findByIntraId(findDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        PChangeDto pChangeDto = PChangeDto.from(pChangeRepository.findByUserAndGame(user, game).orElseThrow(() -> new BusinessException("{invalid.request}")));
        return  pChangeDto;
    }
}
