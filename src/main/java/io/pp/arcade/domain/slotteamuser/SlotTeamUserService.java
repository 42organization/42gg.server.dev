package io.pp.arcade.domain.slotteamuser;

import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
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
}
