package io.gg.arcade.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PchangeFindResposeDto {
    Integer gameId;
    Integer userId;
    Integer pppChange;
    Integer pppResult;


}
