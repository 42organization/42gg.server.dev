package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class UserSearchResultResponseDto {
    List<String> users;
}
