package io.pp.arcade.v1.domain.opponent;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class Opponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String intraId;
    private String nick;
    private String imageUrl;
    private String detail;
    private Boolean isReady;

    public Opponent(String intraId, String nick, String imageUrl, String detail, Boolean isReady) {
        this.intraId = intraId;
        this.nick = nick;
        this.imageUrl = imageUrl;
        this.detail = detail;
        this.isReady = isReady;
    }
}
