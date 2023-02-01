package io.pp.arcade.v1.admin.users.dto;

import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSearchAdminDto {
    private Integer id;
    private String intraId;
    private String statusMessage;
    private RoleType roleType;

    public static UserSearchAdminDto from(User user) {
        UserSearchAdminDto userSearchResponseDto;
        if (user == null) {
            userSearchResponseDto = null;
        } else {
            userSearchResponseDto = UserSearchAdminDto.builder()
                    .id(user.getId())
                    .intraId(user.getIntraId())
                    .statusMessage(user.getStatusMessage())
                    .roleType(user.getRoleType())
                    .build();
        }
        return userSearchResponseDto;
    }

    @Override
    public String toString() {
        return "UserAdminDto{" +
                "id=" + id +
                ", intraId='" + intraId + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", roleType=" + roleType +
                '}';
    }
}
