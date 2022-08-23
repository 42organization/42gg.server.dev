package io.pp.arcade.domain.tmp;

import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slotteamuser.SlotTeamUserService;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.RoleType;
import io.pp.arcade.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class DbUpdate {
    private final SlotService slotService;
    private final GameService gameService;
    private final TeamService teamService;
    private final SlotTeamUserService slotTeamUserService;
    private final UserService userService;
    private final TokenService tokenService;

    @PutMapping("/update/db")
    public void updateDb(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        if (user.getRoleType() == RoleType.ADMIN) {
            slotTeamUserService.saveFromSlot();
            slotTeamUserService.deleteTeamInSlot();
            slotTeamUserService.deleteTeamInGame();
            slotTeamUserService.addSlotInTeam();
        } else {
            throw new BusinessException("E0001");
        }
    }
}
