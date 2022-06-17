package io.pp.arcade.domain.slot;

import io.pp.arcade.domain.admin.dto.create.SlotCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.SlotDeleteDto;
import io.pp.arcade.domain.admin.dto.update.SlotUpdateRequestDto;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SlotService {
    private final SlotRepository slotRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CurrentMatchRepository currentMatchRepository;

    @Transactional
    public void addSlot(SlotAddDto addDto) {
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
                .tableId(addDto.getTableId())
                .time(addDto.getTime())
                .team1(team1)
                .team2(team2)
                .headCount(0)
                .build()
        );
    }

    @Transactional
    public void addUserInSlot(SlotAddUserDto addUserDto) {
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
    public void removeUserInSlot(SlotRemoveUserDto removeUserDto) {
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
    public List<SlotStatusDto> findSlotsStatus(SlotFindStatusDto findDto) {
        LocalDateTime now = findDto.getCurrentTime();
        LocalDateTime todayStartTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        List<Slot> slots = slotRepository.findAllByCreatedAtAfter(todayStartTime);

        User user = userRepository.findById(findDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("?!"));
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user).orElse(null);
        Integer userSlotId = currentMatch == null ? null : currentMatch.getId();

        List<SlotStatusDto> slotDtos = new ArrayList<>();
        for (Slot slot : slots) {
            SlotFilterDto filterDto = SlotFilterDto.builder()
                    .slotId(slot.getId())
                    .userSlotId(userSlotId)
                    .slotTime(slot.getTime())
                    .slotType(slot.getType())
                    .requestType(findDto.getType())
                    .userPpp(user.getPpp())
                    .gamePpp(slot.getGamePpp())
                    .headCount(slot.getHeadCount())
                    .build();
            slotDtos.add(SlotStatusDto.builder()
                    .slotId(slot.getId())
                    .headCount(slot.getHeadCount())
                    .time(slot.getTime())
                    .status(getStatus(filterDto))
                    .build()
            );
        }
        return slotDtos;
    }

    public SlotDto findByTime(LocalDateTime time) {
        Slot slot = slotRepository.findByTime(time).orElse(null);
        SlotDto slotDto = slot == null ? null : SlotDto.from(slot);
        return slotDto;
    }

    public String getStatus(SlotFilterDto dto) {
        /* if currentTime > slotTime
            then status == close
           else if requestType != slotType
            then status == close
           else if abs(userPpp - gamePpp) > 100
            then status == close
           else if slotType == "double" and headCount == MAXCOUNT
            then status == close
         */
        Integer slotId = dto.getSlotId();
        Integer userSlotId = dto.getUserSlotId();
        String slotType = dto.getSlotType();
        LocalDateTime slotTime = dto.getSlotTime();
        String requestType = dto.getRequestType();
        Integer gamePpp = dto.getGamePpp();
        Integer userPpp = dto.getUserPpp();
        Integer headCount = dto.getHeadCount();

        LocalDateTime currentTime = LocalDateTime.now();
        Integer maxCount = 2;
        if (slotType != null && slotType.equals("double")) {
            maxCount = 4;
        }
        String status = "open";
        if (currentTime.isAfter(slotTime)) {
            status = "close";
        } else if (slotId.equals(userSlotId)) {
            status = "mytable";
        } else if (slotType != null && !requestType.equals(slotType)) {
            status = "close";
        } else if (gamePpp != null && Math.abs(userPpp - gamePpp) > 100) {
            status = "close";
        } else if (headCount.equals(maxCount)) {
            status = "close";
        }
        return status;
    }

    public void createSlotByAdmin(SlotCreateRequestDto createDto) {
        slotRepository.save(Slot.builder()
                .tableId(createDto.getTableId())
                .team1(teamRepository.getById(createDto.getTeam1Id()))
                .team2(teamRepository.getById(createDto.getTeam2Id()))
                .time(createDto.getTime())
                .gamePpp(createDto.getGamePpp())
                .headCount(createDto.getHeadCount())
                .type(createDto.getType())
                .build());
    }

    public void updateSlotByAdmin(SlotUpdateRequestDto updateDto) {
        Slot slot = slotRepository.findById(updateDto.getId()).orElseThrow(null);
        slot.setGamePpp(updateDto.getGamePpp());
        slot.setHeadCount(updateDto.getHeadCount());
        slot.setType(updateDto.getType());
    }

    public void deleteSlotByAdmin(SlotDeleteDto deleteDto) {
        Slot slot = slotRepository.findById(deleteDto.getId()).orElseThrow(null);
        slotRepository.delete(slot);
    }

    public List<SlotDto> findSlotByAdmin(Pageable pageable) {
        Page<Slot> slots = slotRepository.findAll(pageable);
        List<SlotDto> slotDtos = slots.stream().map(SlotDto::from).collect(Collectors.toList());
        return slotDtos;
    }
}
