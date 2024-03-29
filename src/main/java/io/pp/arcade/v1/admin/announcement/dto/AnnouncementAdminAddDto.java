package io.pp.arcade.v1.admin.announcement.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class AnnouncementAdminAddDto {
    private String content;
    private String creatorIntraId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime createdTime;
}
