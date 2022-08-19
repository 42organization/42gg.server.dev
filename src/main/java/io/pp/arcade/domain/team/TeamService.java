package io.pp.arcade.domain.team;


import io.pp.arcade.domain.admin.dto.create.TeamCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.TeamDeleteDto;
import io.pp.arcade.domain.admin.dto.update.TeamUpdateDto;
import io.pp.arcade.domain.game.dto.GameUserInfoDto;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.dto.*;
import io.pp.arcade.domain.slotteamuser.DeletedSlotTeamUser;
import io.pp.arcade.domain.slotteamuser.DeletedSlotTeamUserRepository;
import io.pp.arcade.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final SlotTeamUserRepository slotTeamUserRepository;
    private final DeletedSlotTeamUserRepository deletedTeamUserRepository;

    @Transactional
    public TeamDto findById(Integer id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new BusinessException("E0001"));
        return TeamDto.from(team);
    }

    @Transactional
    public List<TeamDto> findAllBySlotId(Integer slotId) {
        List<Team> teams = teamRepository.findAllBySlotId(slotId);
        List<TeamDto> teamDtos = teams.stream().map(TeamDto::from).collect(Collectors.toList());
        return teamDtos;
    }

    @Transactional
    public void addUserInTeam(TeamAddUserDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new BusinessException("E0001"));
        Slot slot = team.getSlot();
        SlotTeamUser slotTeamUser = SlotTeamUser.builder()
                .team(team)
                .slot(slot)
                .user(user)
                .build();

        slotTeamUserRepository.save(slotTeamUser);
        team.setTeamPpp((user.getPpp() + team.getTeamPpp()) / (team.getHeadCount() + 1));
        team.setHeadCount(team.getHeadCount() + 1);
    }

    @Transactional
    public void removeUserInTeam(TeamRemoveUserDto dto) {
        SlotTeamUser slotTeamUser = slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(dto.getSlotId(), dto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));

        Team team = slotTeamUser.getTeam();
        User user = slotTeamUser.getUser();
        Slot slot = team.getSlot();

        deletedTeamUserRepository.save(DeletedSlotTeamUser.builder()
                .slot(slot)
                .team(team)
                .user(user)
                .build());
        slotTeamUserRepository.delete(slotTeamUser);
        Integer headCountResult = team.getHeadCount() - 1; // entity라 반영이 안되어서 미리 뺀 값을 써줘야함
        if (headCountResult == 0) {
            team.setTeamPpp(0);
        } else {
            team.setTeamPpp((team.getTeamPpp() * team.getHeadCount() - user.getPpp()) / headCountResult);
        }
        team.setHeadCount(headCountResult);
    }


    @Transactional
    public void saveGameResultInTeam(TeamSaveGameResultDto dto) {
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new BusinessException("E0001"));

        team.setScore(dto.getScore());
        team.setWin(dto.getWin());
    }

    @Transactional
    public void modifyGameResultInTeam(TeamModifyGameResultDto dto) {
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new BusinessException("E0001"));

        team.setScore(dto.getScore());
        team.setWin(dto.getWin());
    }

    @Transactional
    public SlotTeamUser findSlotTeamUserByTeamAndUser(Integer teamId, Integer userId) {
        return slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(teamId, userId).orElseThrow(() -> new BusinessException("E0001"));
    }

    @Transactional
    public TeamPosDto findUsersByTeamPos(SlotDto slotDto, UserDto curUser) {
        List<SlotTeamUser> slotTeamUsers = slotTeamUserRepository.findAllBySlotId(slotDto.getId());

        SlotTeamUser currentUser = slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slotDto.getId(), curUser.getId()).orElseThrow(() -> new BusinessException("E0001"));

        List<GameUserInfoDto> myTeamUserInfos = new ArrayList<>();
        List<GameUserInfoDto> enemyTeamUserInfos = new ArrayList<>();
        for (SlotTeamUser slotTeamUser : slotTeamUsers) {
            if (slotTeamUser.getTeam().equals(currentUser.getTeam())) {
                myTeamUserInfos.add(GameUserInfoDto.from(slotTeamUser.getUser()));
            } else {
                enemyTeamUserInfos.add(GameUserInfoDto.from(slotTeamUser.getUser()));
            }
        }

        TeamPosDto dto = TeamPosDto.builder()
                .myTeam(myTeamUserInfos)
                .enemyTeam(enemyTeamUserInfos)
                .build();
        return dto;
    }

    @Transactional
    public void createTeamByAdmin(TeamCreateRequestDto teamCreateDto) {
        teamRepository.save(Team.builder()
                .teamPpp(teamCreateDto.getTeamPpp())
                .headCount(teamCreateDto.getHeadCount())
                .score(teamCreateDto.getScore())
                .build());
    }

    @Transactional
    public void updateTeamByAdmin(TeamUpdateDto teamUpdateDto) {
        Team team = teamRepository.findById(teamUpdateDto.getTeamId()).orElseThrow();
        List<String> userIds = List.of(new String[]{teamUpdateDto.getUser1Id(), teamUpdateDto.getUser2Id()});
        for (String id : userIds) {
            userRepository.findByIntraId(id).ifPresent(user -> slotTeamUserRepository.save(SlotTeamUser.builder()
                    .team(team)
                    .user(user)
                    .build()));
        }
//        team.setUser1(teamUpdateDto.getUser1Id() == null ? null : userRepository.findByIntraId(teamUpdateDto.getUser1Id()).orElse(null));
//        team.setUser2(teamUpdateDto.getUser2Id() == null ? null : userRepository.findByIntraId(teamUpdateDto.getUser2Id()).orElse(null));
        team.setTeamPpp(teamUpdateDto.getTeamPpp());
        team.setHeadCount(teamUpdateDto.getHeadCount());
        team.setScore(teamUpdateDto.getScore());
        team.setWin(teamUpdateDto.getWin());
    }

    @Transactional
    public List<TeamDto> findTeamByAdmin(Pageable pageable) {
        Page<Team> teams = teamRepository.findAllByOrderByIdDesc(pageable);
        List<TeamDto> teamDtos = teams.stream().map(TeamDto::from).collect(Collectors.toList());
        return teamDtos;
    }

    @Transactional
    public void deleteTeamByAdmin(TeamDeleteDto teamDeleteDto) {
        teamRepository.deleteById(teamDeleteDto.getTeamId());
    }

    /* 아이디를 통한 팀 추가 */
}
