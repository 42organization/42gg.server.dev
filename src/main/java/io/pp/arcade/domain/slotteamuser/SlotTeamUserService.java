package io.pp.arcade.domain.slotteamuser;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SlotTeamUserService {
    private final SlotTeamUserRepository slotTeamUserRepository;
    private final SlotRepository slotRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    @Transactional
    public List<SlotTeamUserDto> findAllByTeamId(Integer teamId) {
        return slotTeamUserRepository.findAllByTeamId(teamId).stream().map(SlotTeamUserDto::from).collect(Collectors.toList());
    }

    @Transactional
    public List<SlotTeamUserDto> findAllByUserId(Integer userId) {
        return slotTeamUserRepository.findAllByUserId(userId).stream().map(SlotTeamUserDto::from).collect(Collectors.toList());
    }

    @Transactional
    public List<SlotTeamUserDto> findAllBySlotId(Integer slotId) {
        List<SlotTeamUser> slotTeamUsers = slotTeamUserRepository.findAllBySlotId(slotId);
        List<SlotTeamUserDto> slotTeamUserDtos = slotTeamUsers.stream().map(SlotTeamUserDto::from).collect(Collectors.toList());
        return slotTeamUserDtos;
    }

    @Transactional
    public List<SlotTeamUserDto> findAllByAdmin(Pageable pageable) {
        Page<SlotTeamUser> slotTeamUsers = slotTeamUserRepository.findAllByOrderByTeamUserId(pageable);
        List<SlotTeamUserDto> slotTeamUserDtos = slotTeamUsers.stream().map(SlotTeamUserDto::from).collect(Collectors.toList());
        return slotTeamUserDtos;
    }
}
