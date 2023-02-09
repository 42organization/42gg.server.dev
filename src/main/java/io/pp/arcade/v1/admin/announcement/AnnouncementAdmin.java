package io.pp.arcade.v1.admin.announcement;

import io.pp.arcade.v1.global.util.BaseTimeEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class AnnouncementAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content", length=10000)
    private String content;

    @Column(name = "is_del")
    private Boolean isDel;

    @Column(name = "creator_intra_id")
    private String creatorIntraId;

    @Column(name = "deleter_intra_id")
    private String deleterIntraId;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @Builder
    AnnouncementAdmin(String content, String creatorIntraId, LocalDateTime createdTime) {
        this.content = content;
        this.creatorIntraId = creatorIntraId;
        this.deleterIntraId = null;
        this.createdTime = createdTime;
        this.deletedTime = null;
        this.isDel = false;
    }

    public void update(String creatorIntraId, LocalDateTime createdTime) {
        this.creatorIntraId = creatorIntraId;
        this.createdTime = createdTime;
        this.isDel = true;
    }
}
