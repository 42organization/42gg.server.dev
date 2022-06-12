package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
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
    public List<PChangeDto> findPChangeByUserId(PChangeFindDto findDto){
        User user = userRepository.findById(findDto.getUserId()).orElseThrow();
        List<PChange> pChangeList = pChangeRepository.findAllByUserOrderByIdDesc(user);
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public PChangeDto findPChangeByUserAndGame(PChangeFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGameId()).orElseThrow();
        User user = userRepository.findById(findDto.getUserId()).orElseThrow();
        PChangeDto pChangeDto = PChangeDto.from(pChangeRepository.findByUserAndGame(user, game).orElseThrow());
        return  pChangeDto;
    }
}
