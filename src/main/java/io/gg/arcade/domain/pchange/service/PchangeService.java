package io.gg.arcade.domain.pchange.service;

import io.gg.arcade.domain.pchange.dto.PchangeFindRequestDto;
import io.gg.arcade.domain.pchange.dto.PchangeFindResposeDto;
import io.gg.arcade.domain.pchange.dto.PchangeAddRequestDto;

public interface PchangeService {
    void addPchange(PchangeAddRequestDto pchangeSaveDto);
    void modifyPchange();
    void deletePchange();
    PchangeFindResposeDto findPchanges(PchangeFindRequestDto findDto);
}
