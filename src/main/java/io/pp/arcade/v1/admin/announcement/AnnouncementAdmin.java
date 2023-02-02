package io.pp.arcade.v1.admin.announcement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class AnnouncementAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "is_del")
    private Boolean isDel;

    @Builder
    AnnouncementAdmin(String content) {
        this.content = content;
        this.isDel = isDel == null ? false : isDel;
    }
}
