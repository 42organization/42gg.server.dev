package io.pp.arcade.domain.tmp;

import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.RoleType;
import io.pp.arcade.global.util.HeaderUtil;
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
