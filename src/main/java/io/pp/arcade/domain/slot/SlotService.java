package io.pp.arcade.domain.slot;

import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SlotService {
    private final SlotRepository slotRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addSlot(SlotAddRequestDto addDto) {
        Team team1 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .build()
        );

        Team team2 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .build()
        );

        slotRepository.save(Slot.builder()
                .time(addDto.getTime())
                .team1(team1)
                .team2(team2)
                .headCount(0)
                .build()
        );
    }

    @Transactional
    public void addUserInSlot(SlotAddUserRequestDto addUserDto) {
        Slot slot = slotRepository.findById(addUserDto.getSlotId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        Integer headCountResult = slot.getHeadCount() + 1; // entity라 반영이 안되어서 미리 뺀 값을 써줘야함
        if (slot.getHeadCount() == 0) {
            slot.setType(addUserDto.getType());
            slot.setGamePpp(addUserDto.getJoinUserPpp());
        } else {
            slot.setGamePpp((addUserDto.getJoinUserPpp() + slot.getGamePpp() * slot.getHeadCount()) / headCountResult);
        }
        slot.setHeadCount(headCountResult);
    }

    @Transactional
    public void removeUserInSlot(SlotRemoveUserRequestDto removeUserDto) {
        Slot slot = slotRepository.findById(removeUserDto.getSlotId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        Integer headCountResult = slot.getHeadCount() - 1; // entity라 반영이 안되어서 미리 뺀 값을 써줘야함
        if (headCountResult == 0) {
            slot.setType(null);
            slot.setGamePpp(null);
        } else {
            slot.setGamePpp((slot.getGamePpp() * slot.getHeadCount() - removeUserDto.getExitUserPpp()) / headCountResult);
        }
        slot.setHeadCount(headCountResult);
    }

    public SlotDto findSlotById(Integer slotId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("?"));
        return SlotDto.from(slot);
    }

    //mytable 테이블 추가하기!
    public List<SlotResponseDto> findSlotsStatus(SlotFindStatusRequestDto findDto) {
        LocalDateTime now = findDto.getCurrentTime();
        LocalDateTime todayStartTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        List<Slot> slots = slotRepository.findAllByCreatedDateAfter(todayStartTime);

        User user = userRepository.findById(findDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("?!"));
        List<SlotResponseDto> slotDtos = new ArrayList<>();
        for (Slot slot : slots) {
            slotDtos.add(SlotResponseDto.builder()
                    .slotId(slot.getId())
                    .headCount(slot.getHeadCount())
                    .time(slot.getTime())
                    .status(getStatus(slot.getTime(), slot.getType(), findDto.getType(), user.getPpp(), slot.getGamePpp(), slot.getHeadCount()))
                    .build()
            );
        }
        return slotDtos;
    }

    private String getStatus(LocalDateTime slotTime, String slotType, String requestType, Integer userPpp, Integer gamePpp, Integer headCount) {
        /* if currentTime > slotTime
            then status == close
           else if requestType != slotType
            then status == close
           else if abs(userPpp - gamePpp) > 100
            then status == close
           else if slotType == "double" and headCount == MAXCOUNT
            then status == close
         */
        LocalDateTime currentTime = LocalDateTime.now();
        Integer maxCount = 2;
        if (slotType != null && slotType.equals("double")) {
            maxCount = 4;
        }
        String status = "open";
        if (currentTime.isAfter(slotTime)) {
            status = "close";
        } else if (slotType != null && !requestType.equals(slotType)) {
            status = "close";
        } else if (gamePpp != null && Math.abs(userPpp - gamePpp) > 100) {
            status = "close";
        } else if (headCount.equals(maxCount)) {
            status = "close";
        }
        return status;
    }
}
