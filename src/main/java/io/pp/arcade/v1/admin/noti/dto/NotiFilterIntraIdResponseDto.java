package io.pp.arcade.v1.admin.noti.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class NotiFilterIntraIdResponseDto {

    private List<NotiResponseDto> notiList;
    private int totalPage;
    private int currentPage;

}
