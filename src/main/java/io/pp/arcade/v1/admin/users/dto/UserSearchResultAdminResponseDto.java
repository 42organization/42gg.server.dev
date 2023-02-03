package io.pp.arcade.v1.admin.users.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserSearchResultAdminResponseDto {
    List<String> users;

    @Override
    public String toString() {
        return "UserSearchResultResponseDto{" +
                "users=" + users +
                '}';
    }
}
