package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.dto.PChangeAddRequestDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class PChangeService {
    private final PChangeRepository pChangeRepository;
    private final GameRepository gameRepository;

    @Transactional
    public void addPChange(PChangeAddRequestDto addDto) {
        pChangeRepository.save(PChange.builder()
                .gameId(addDto.getGameId())
                .userId(addDto.getUserId())
                .pppChange(addDto.getPppChange())
                .pppResult(addDto.getPppResult())
                .build()
        );

    }

    @Transactional
    public void findPChange(PChangeFindRequestDto findDto) {}
}
