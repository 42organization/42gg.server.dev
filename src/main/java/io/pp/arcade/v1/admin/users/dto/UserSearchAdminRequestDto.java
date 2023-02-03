package io.pp.arcade.v1.admin.users.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class UserSearchAdminRequestDto {
    @NotNull(message = "")
    String intraId;

    @Override
    public String toString() {
        return "UserSearchRequestDto{" +
                "intraId='" + intraId + '\'' +
                '}';
    }
}
