package io.pp.arcade.v1.admin.announcement.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class AnnouncementAdminUpdateDto {
    private String deleterIntraId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedTime;
}
