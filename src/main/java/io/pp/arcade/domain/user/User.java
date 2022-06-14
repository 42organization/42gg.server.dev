package io.pp.arcade.domain.user;

import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "intra_id")
    private String intraId;

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

    @Builder
    public User(String intraId, String imageUri, RacketType racketType, String statusMessage, Integer ppp) {
        this.intraId = intraId;
        this.imageUri = imageUri;
        this.racketType = racketType;
        this.statusMessage = statusMessage;
        this.ppp = ppp;
    }
}
