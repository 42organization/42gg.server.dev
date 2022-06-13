package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserSearchResultResponseDto {
    List<String> users;
}
