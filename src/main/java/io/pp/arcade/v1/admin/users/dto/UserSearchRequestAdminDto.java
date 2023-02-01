package io.pp.arcade.v1.admin.users.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class UserSearchRequestAdminDto {
    @NotNull(message = "")
    String intraId;

    @Override
    public String toString() {
        return "UserSearchRequestDto{" +
                "intraId='" + intraId + '\'' +
                '}';
    }
}
