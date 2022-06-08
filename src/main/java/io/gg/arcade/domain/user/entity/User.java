package io.gg.arcade.domain.user.entity;


import io.gg.arcade.common.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "intra_id")
    @NotNull
    String intraId;

    @Column(name = "user_img_uri")
    String userImgUri;

    @Column(name = "racket_type")
    String racketType;

    @Column(name = "status_message")
    String statusMessage;
    
    @Column(name = "is_playing")
    Boolean isPlaying;

    @Setter
    @NotNull
    @Column(name = "ppp")
    Integer ppp;

    @Builder
    public User(Integer id, String intraId, String userImgUri, String racketType, String statusMessage, Boolean isPlaying, Integer ppp) {
        this.id = id;
        this.intraId = intraId;
        this.userImgUri = userImgUri;
        this.racketType = racketType;
        this.statusMessage = statusMessage;
        this.isPlaying = isPlaying;
        this.ppp = ppp;
    }
}
