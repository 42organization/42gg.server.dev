package io.pp.arcade.v1.domain.opponent;

import io.pp.arcade.v1.domain.opponent.Opponent;
import io.pp.arcade.v1.domain.opponent.OpponentRepository;
import io.pp.arcade.v1.domain.slot.dto.OpponentResponseDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
