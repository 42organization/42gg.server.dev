package io.pp.arcade.admin.management;

import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.season.SeasonService;
import io.pp.arcade.domain.season.dto.SeasonDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.scheduler.CurrentMatchUpdater;
import io.pp.arcade.global.scheduler.GameGenerator;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.RoleType;
import io.pp.arcade.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
public class ManagementController {
    private final UserService userService;
    private final GameService gameService;
    private final SeasonService seasonService;
    private final CurrentMatchUpdater currentMatchUpdater;
    private final GameGenerator gameGenerator;

    /* 관리자 관리 */
    @GetMapping("/admin/admin")
    public String adminPage(Model model, HttpServletRequest request) {
        List<UserDto> userList = userService.findAllByRoleType(RoleType.USER);
        List<UserDto> adminList = userService.findAllByRoleType(RoleType.ADMIN);


        model.addAttribute("adminList", adminList);
        model.addAttribute("userList", userList);
        model.addAttribute("token", HeaderUtil.getAccessToken(request));
        return "admin_management";
    }

    /* 게임 결과 관리 */
    @GetMapping("/admin/game")
    public String gameResultPage(Model model, HttpServletRequest request) {
        List<GameDto> singleGameList = gameService.findGameByTypeByAdmin(Pageable.unpaged(), GameType.SINGLE);
        List<GameDto> bungleGameList = gameService.findGameByTypeByAdmin(Pageable.unpaged(), GameType.BUNGLE);
        model.addAttribute("singleGameList", singleGameList);
        model.addAttribute("bungleGameList", bungleGameList);
        model.addAttribute("token", HeaderUtil.getAccessToken(request));
        return "game_management";
    }

    @GetMapping("/admin/game_result")
    public GameDto gameResultCheck(Model model, @RequestParam Integer gameId, HttpServletRequest request) {
        GameDto game = gameService.findById(gameId);
        return game;
    }

    /* 시즌 관리 */
    @GetMapping("/admin/season")
    public String seasonPage(Model model, HttpServletRequest request) {
        List<SeasonDto> seasonList = seasonService.findSeasonsByAdmin(Pageable.unpaged());

        model.addAttribute("seasonList", seasonList);
        model.addAttribute("token", HeaderUtil.getAccessToken(request));
        return "season_management";
    }

    /* 스케쥴러 관리 */
    @GetMapping("/admin/scheduler")
    public String schedularPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentCron", currentMatchUpdater.getCron());
        model.addAttribute("gameCron", gameGenerator.getCron());
        model.addAttribute("token", HeaderUtil.getAccessToken(request));
        return "scheduler_management";
    }

    @GetMapping("/admin")
    public String mainPage() {
        return "layout/admin";
    }
}
