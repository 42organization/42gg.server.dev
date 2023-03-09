package io.pp.arcade.v1.domain.announcement;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Announcement {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "content", length=10000)
    private String content;

    @NotNull
    @Column(name = "is_del")
    private Boolean isDel;

    @NotNull
    @Column(name = "creator_intra_id")
    private String creatorIntraId;

    @NotNull
    @Column(name = "deleter_intra_id")
    private String deleterIntraId;

    @NotNull
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @NotNull
    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @Builder
    Announcement(String content, String creatorIntraId, LocalDateTime createdTime) {
        this.content = content;
        this.creatorIntraId = creatorIntraId;
        this.deleterIntraId = null;
        this.createdTime = createdTime;
        this.deletedTime = null;
        this.isDel = false;
    }

    public void update(String deleterIntraId, LocalDateTime deletedTime) {
        this.deleterIntraId = deleterIntraId;
        this.deletedTime = deletedTime;
        this.isDel = true;
    }
}
