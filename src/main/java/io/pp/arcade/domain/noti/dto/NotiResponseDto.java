package io.pp.arcade.domain.noti.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NotiResponseDto<T> {
    private List<T> notifications;
}
