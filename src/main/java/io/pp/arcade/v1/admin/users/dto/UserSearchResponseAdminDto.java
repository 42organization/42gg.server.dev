package io.pp.arcade.v1.admin.users.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserSearchResponseAdminDto {
    private List<UserSearchAdminDto> userSearchAdminDtos;
    private Integer totalPage;
    private Integer currentPage;
}
