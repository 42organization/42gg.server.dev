package io.gg.arcade.domain.user.entity;

import io.gg.arcade.domain.user.dto.UserRequestDto;
import io.gg.arcade.global.entity.BaseTimeEntity;
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
    private Integer id;

    @NotNull
    @Column(name = "intra_id")
    private String intraId;

    @NotNull
    @Column
    private String userImageUri;

    @NotNull
    @Column
    private String racketType;

    @NotNull
    @Column
    private Boolean isPlaying;

    @Column
    private String statusMessage;

    @Builder
    public User(String intraId, String userImageUri, String racketType, Boolean isPlaying, String statusMessage) {
        this.intraId = intraId;
        this.userImageUri = userImageUri;
        this.racketType = racketType;
        this.isPlaying = isPlaying;
        this.statusMessage = statusMessage;
    }
}
