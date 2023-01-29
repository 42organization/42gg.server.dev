package io.pp.arcade.v1.domain.announcement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "link")
    private String link;

    @Column(name = "is_del")
    private Boolean isDel;

    @Builder
    public Announcement(String title, String content, String link, Boolean isDel) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.isDel = isDel == null ? false : isDel;
    }
}
