package io.pp.arcade.v1.admin.dto;

import io.pp.arcade.v1.global.type.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AdminCheckerDto implements Serializable {
    private RoleType roleType;
    private String intraId;
}
