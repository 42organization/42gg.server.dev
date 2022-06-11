package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
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

    @Transactional
    public void addPChange(PChangeAddDto addDto) {
        pChangeRepository.save(PChange.builder()
                .gameId(addDto.getGameId())
                .userId(addDto.getUserId())
                .pppChange(addDto.getPppChange())
                .pppResult(addDto.getPppResult())
                .build()
        );
    }

    @Transactional
    public List<PChangeDto> findPChangeByGameId(PChangeFindDto findDto) {
        List<PChange> pChangeList = pChangeRepository.findAllByGameId(findDto.getGameId()).orElseThrow(() -> new IllegalArgumentException("?"));        
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }

    @Transactional
    public List<PChangeDto> findPChangeByUserId(PChangeFindDto findDto){
        List<PChange> pChangeList = pChangeRepository.findAllByUserId(findDto.getUserId());
        return pChangeList.stream().map(PChangeDto::from).collect(Collectors.toList());
    }
}
