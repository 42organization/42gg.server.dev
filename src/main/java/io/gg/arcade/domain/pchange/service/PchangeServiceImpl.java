package io.gg.arcade.domain.pchange.service;

import io.gg.arcade.domain.pchange.dto.PchangeFindRequestDto;
import io.gg.arcade.domain.pchange.dto.PchangeFindResposeDto;
import io.gg.arcade.domain.pchange.dto.PchangeAddRequestDto;
import io.gg.arcade.domain.pchange.entity.PChange;
import io.gg.arcade.domain.pchange.repository.PchangeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class PchangeServiceImpl implements PchangeService{
    private final PchangeRepository pchangeRepository;

    @Override
    @Transactional
    public void addPchange(PchangeAddRequestDto dto) {
        pchangeRepository.save(
                dto.toEnity(
                        dto.getGameId(),
                        dto.getUserId(),
                        dto.getPppChange(),
                        dto.getPppResult()
                ));
    }

    @Override
    @Transactional
    public void modifyPchange() {

    }

    @Override
    @Transactional
    public void deletePchange() {

    }

    @Override
    @Transactional
    public PchangeFindResposeDto findPchanges(PchangeFindRequestDto findDto) {
        PChange pChange = pchangeRepository.findByUserIdAndGameId(findDto.getUserId(), findDto.getGameId());
        return (
                PchangeFindResposeDto.builder()
                .gameId(pChange.getGameId())
                .userId(pChange.getUserId())
                .pppChange(pChange.getPppChange())
                .pppResult(pChange.getPppResult())
                .build()
        );
    }
}
