package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.dto.PChangeAddRequestDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<PChangeDto> findPChange(PChangeFindRequestDto findDto) {
        List<PChange> pChangeList = pChangeRepository.findAllByGameId(findDto.getGameId()).orElseThrow(() -> new IllegalArgumentException("?"));        
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }
}
