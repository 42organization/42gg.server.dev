package io.pp.arcade.v1.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserSearchResultResponseDto {
    List<String> users;

    @Override
    public String toString() {
        return "UserSearchResultResponseDto{" +
                "users=" + users +
                '}';
    }
}
