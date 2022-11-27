package io.pp.arcade.v1.domain.opponent;

import io.pp.arcade.v1.domain.opponent.dto.OpponentResponseDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class OpponentService {
    private final OpponentRepository opponentRepository;

    @Transactional(readOnly = true)
    public OpponentResponseDto findByIntraId(String intraId) {
        Opponent opponent = opponentRepository.findByIntraId(intraId).orElseThrow((() -> new BusinessException("E0001")));
        return OpponentResponseDto.from(opponent);
    }

    @Transactional(readOnly = true)
    public List<OpponentResponseDto> findRandom3Opponents() {
        List<OpponentResponseDto> responseDtoList = new ArrayList<>();
        List<Opponent> opponents = opponentRepository.findAllByIsReady(true);
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        while (responseDtoList.size() < 3) {
            OpponentResponseDto opponent = OpponentResponseDto.from(opponents.get(Math.abs(random.nextInt()) % opponents.size()));
            if (responseDtoList.stream().noneMatch(listedOpponent -> listedOpponent.getIntraId().equals(opponent.getIntraId()))) {
                responseDtoList.add(opponent);
            }
        }
        return responseDtoList;
    }
}
