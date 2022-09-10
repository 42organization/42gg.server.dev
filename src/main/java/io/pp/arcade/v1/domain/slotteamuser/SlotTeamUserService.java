package io.pp.arcade.v1.domain.slotteamuser;

import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.v1.domain.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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

    public SlotTeamUserDto findTeamBySlotAndUser(Integer slotId, Integer userId) {
        return slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slotId, userId).map(SlotTeamUserDto::from).orElse(null);
    }
}
