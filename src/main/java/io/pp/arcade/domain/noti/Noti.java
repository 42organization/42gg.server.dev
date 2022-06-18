package io.pp.arcade.domain.noti;

import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Noti extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@JoinColumn(name = "user_id", referencedColumnName = "intra_id")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @NotNull
    @Column(name = "noti_type")
    private String notiType;

    @Column(name = "message")
    private String message;

    @Setter
    @NotNull
    @Column(name = "is_checked")
    private Boolean isChecked;

    @Builder
    public Noti(User user, Slot slot, String notiType, String message, Boolean isChecked) {
        this.user = user;
        this.slot = slot;
        this.notiType = notiType;
        this.message = message;
        this.isChecked = isChecked;
    }
}
