package io.pp.arcade.v1.admin.users.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class UserDetailRequestAdminDto {
    Integer userId;

    public UserDetailRequestAdminDto(Integer userId) {
        this.userId = userId;
    }

    public UserDetailRequestAdminDto() {
    }

    @Override
    public String toString() {
        return "UserDetailRequestAdminDto{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
