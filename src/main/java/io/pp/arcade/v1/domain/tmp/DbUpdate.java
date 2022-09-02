package io.pp.arcade.v1.domain.tmp;

import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.RoleType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class DbUpdate {
    private final DbUpdateService dbUpdateService;
    private final TokenService tokenService;

    @PutMapping("/update/db")
    public void updateDb(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        if (user.getRoleType() == RoleType.ADMIN) {
            dbUpdateService.saveFromSlot();
            dbUpdateService.deleteTeamInSlot();
            dbUpdateService.deleteTeamInGame();
            dbUpdateService.addSlotInTeam();
        } else {
            throw new BusinessException("E0001");
        }
    }
}
