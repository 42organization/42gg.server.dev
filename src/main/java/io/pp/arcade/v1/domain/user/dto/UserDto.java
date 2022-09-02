package io.pp.arcade.v1.domain.user.dto;

import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class UserDto {
    private Integer id;
    private String intraId;
    private String eMail;
    private String imageUri;
    private RacketType racketType;
    private String statusMessage;
    private RoleType roleType;
    private Integer ppp;
    private Integer totalExp;


    public static UserDto from(User user) {
        UserDto userDto;
        if (user == null) {
            userDto = null;
        } else {
            userDto = UserDto.builder()
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
        return userDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(intraId, userDto.intraId) && Objects.equals(imageUri, userDto.imageUri) && racketType == userDto.racketType && Objects.equals(statusMessage, userDto.statusMessage) && Objects.equals(ppp, userDto.ppp) && Objects.equals(totalExp, userDto.totalExp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, intraId, imageUri, racketType, statusMessage, ppp, totalExp);
    }

    @Override
    public String toString() {
        return "UserDto{" +
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
