package io.pp.arcade.v1.admin.users.dto;

import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class UserAdminDto {
    private Integer id;
    private String intraId;
    private String eMail;
    private String imageUri;
    private RacketType racketType;
    private String statusMessage;
    private RoleType roleType;
    private Integer ppp;
    private Integer totalExp;


    public static UserAdminDto from(User user) {
        UserAdminDto userAdminDto;
        if (user == null) {
            userAdminDto = null;
        } else {
            userAdminDto = UserAdminDto.builder()
                    .id(user.getId())
                    .intraId(user.getIntraId())
                    .eMail(user.getEMail())
                    .imageUri(user.getImageUri())
                    .racketType(user.getRacketType())
                    .statusMessage(user.getStatusMessage())
                    .roleType(user.getRoleType())
                    .ppp(user.getPpp())
                    .totalExp(user.getTotalExp())
                    .build();
        }
        return userAdminDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAdminDto userAdminDto = (UserAdminDto) o;
        return Objects.equals(id, userAdminDto.id) && Objects.equals(intraId, userAdminDto.intraId) && Objects.equals(imageUri, userAdminDto.imageUri) && racketType == userAdminDto.racketType && Objects.equals(statusMessage, userAdminDto.statusMessage) && Objects.equals(ppp, userAdminDto.ppp) && Objects.equals(totalExp, userAdminDto.totalExp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, intraId, imageUri, racketType, statusMessage, ppp, totalExp);
    }

    @Override
    public String toString() {
        return "UserAdminDto{" +
                "id=" + id +
                ", intraId='" + intraId + '\'' +
                ", eMail='" + eMail + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", racketType=" + racketType +
                ", statusMessage='" + statusMessage + '\'' +
                ", roleType=" + roleType +
                ", ppp=" + ppp +
                ", totalExp=" + totalExp +
                '}';
    }
}
