package io.pp.arcade.v1.admin.users.dto;

import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserUpdateRequesAdmintDto {
    private Integer userId;
    private String intraId;
//    private MultipartFile imgFile;
    private RacketType racketType;
    private String statusMessage;
    private Integer wins;
    private Integer losses;
    private Integer ppp;
    private String email;
    private RoleType roleType;

    @Override
    public String toString() {
        return "UserAdminDto{" + '\'' +
                "id=" + userId + '\'' +
                "intraId=" + intraId + '\'' +
                ", eMail=" + email + '\'' +
//                ", imgFile='" + imgFile.getOriginalFilename() + '\'' +
                ", racketType=" + racketType +
                ", statusMessage='" + statusMessage + '\'' +
                ", roleType=" + roleType +
                ", ppp=" + ppp +
                '}';
    }
}
