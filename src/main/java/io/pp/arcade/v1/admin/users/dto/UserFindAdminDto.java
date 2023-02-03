package io.pp.arcade.v1.admin.users.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserFindAdminDto {
    String intraId;
    Integer userId;

    @Override
    public String toString() {
        return "UserFindDto{" +
                "intraId='" + intraId + '\'' +
                ", userId=" + userId +
                '}';
    }
}