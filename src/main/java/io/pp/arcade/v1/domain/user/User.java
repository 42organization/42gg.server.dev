package io.pp.arcade.v1.domain.user;

import io.pp.arcade.v1.global.type.RoleType;
import io.pp.arcade.v1.global.util.BaseTimeEntity;
import io.pp.arcade.v1.global.type.RacketType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Setter
    @Column(name = "intra_id")
    private String intraId;

    @Setter
    @Column(name = "e_mail")
    private String eMail;

    @Setter
    @Column(name = "image_uri")
    private String imageUri;

    @Setter
    @Column(name = "racket_type")
    private RacketType racketType;

    @Setter
    @NotNull
    @Column(name = "status_message")
    private String statusMessage;

    @Setter
    @NotNull
    @Column(name = "ppp")
    private Integer ppp;

    @Setter
    @NotNull
    @Column(name = "role_type")
    private RoleType roleType;

    @Setter
    //    @NotNull
    @Column(name = "total_exp")
    private Integer totalExp;

    @Builder
    public User(String intraId, String eMail, String imageUri, RacketType racketType, String statusMessage, Integer ppp, RoleType roleType, Integer totalExp) {
        this.intraId = intraId;
        this.eMail = eMail;
        this.imageUri = imageUri;
        this.racketType = racketType;
        this.statusMessage = statusMessage;
        this.roleType = roleType;
        this.ppp = ppp;
        this.totalExp = totalExp;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", intraId='" + intraId + '\'' +
                ", eMail='" + eMail + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", racketType=" + racketType +
                ", statusMessage='" + statusMessage + '\'' +
                ", ppp=" + ppp +
                ", roleType=" + roleType +
                ", totalExp=" + totalExp +
                '}';
    }
}
