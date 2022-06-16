package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.TeamAllDto;
import io.pp.arcade.domain.admin.dto.create.TeamCreateDto;
import io.pp.arcade.domain.admin.dto.delete.TeamDeleteDto;
import io.pp.arcade.domain.admin.dto.update.TeamUpdateDto;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class TeamAdminControllerImpl implements TeamAdminController {
    private final TeamService teamService;

    @Override
    @PostMapping(value = "/team")
    public void teamCreate(TeamCreateDto teamCreateDto, HttpServletRequest request) {
        teamService.createTeamByAdmin(teamCreateDto);
    }

    @Override
    @PutMapping(value = "/team/{id}")
    public void teamUpdate(Integer id, TeamUpdateDto teamUpdateDto, HttpServletRequest request) {
        teamService.updateTeamByAdmin(teamUpdateDto);
    }

    @Override
    @DeleteMapping(value = "/team/{id}")
    public void teamDelete(Integer id, HttpServletRequest request) {
        teamService.deleteTeamByAdmin(TeamDeleteDto.builder().id(id).build());
    }

    @Override
    @GetMapping(value = "/team")
    public void teamAll(Pageable pageable, HttpServletRequest request) {
        List<TeamDto> teamDtos = teamService.findTeamByAdmin(pageable);
    }
}
