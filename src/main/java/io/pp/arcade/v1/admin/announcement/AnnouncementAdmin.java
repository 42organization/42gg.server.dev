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

    @Column(name = "creator_intra_id")
    private String creatorIntraId;

    @Column(name = "deleter_intra_id")
    private String deleterIntraId;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @Builder
    AnnouncementAdmin(String content, String creatorIntraId, String deleterIntraId,
                      LocalDateTime createdTime, LocalDateTime deletedTime) {
        this.content = content;
        this.creatorIntraId = creatorIntraId;
        this.deleterIntraId = deleterIntraId;
        this.createdTime = createdTime;
        this.deletedTime = deletedTime;
        this.isDel = isDel == null ? false : isDel;
    }

//    @Builder
//    AnnouncementAdmin(String content, String creatorIntraId, String deleterIntraId) {
//        this.content = content;
//        this.creatorIntraId = creatorIntraId;
//        this.deleterIntraId = deleterIntraId;
//        this.isDel = isDel == null ? false : isDel;
//    }
}
