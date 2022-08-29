package io.pp.arcade.domain.slotteamuser;

import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.domain.team.dto.TeamPosDto;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SlotTeamUserService {
    private final SlotTeamUserRepository slotTeamUserRepository;

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
        return slotTeamUserRepository.findAllBySlotId(slotId).stream().map(SlotTeamUserDto::from).collect(Collectors.toList());
    }

    public SlotTeamUserDto findTeamBySlotAndUser(Integer slotId, Integer userId) {
        return slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slotId, userId).map(SlotTeamUserDto::from).orElse(null);
    }
}
