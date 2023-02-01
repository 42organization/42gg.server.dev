package io.pp.arcade.v1.admin.noti.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
public class NotiAllResponseDto {

    private List<NotiResponseDto> notiList;
    private int totalPage;
    private int currentPage;

}
