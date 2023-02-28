package io.pp.arcade.v1.domain.slot;

import io.pp.arcade.v1.admin.dto.create.SlotCreateRequestDto;
import io.pp.arcade.v1.admin.dto.delete.SlotDeleteDto;
import io.pp.arcade.v1.admin.dto.update.SlotUpdateDto;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;

import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.SlotStatusType;
import io.pp.arcade.v1.domain.slot.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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
    private final SeasonRepository seasonRepository;
    private final CurrentMatchRepository currentMatchRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void addSlot(SlotAddDto addDto) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(addDto.getTableId())
                .time(addDto.getTime())
                .headCount(0)
                .mode(Mode.BOTH)
                .build());

        teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());

        teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());

        /*
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(addDto.getTableId())
                .time(addDto.getTime())
                .headCount(0)
                .build()
        );

        for (int i = 0; i < 2; i++) {
            generateNewTeamInSlotTeam(slot);
        }
        */
    }

    @Transactional
    public void addUserInSlot(SlotAddUserDto addUserDto) {
        Slot slot = slotRepository.findById(addUserDto.getSlotId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer headCountResult = slot.getHeadCount() + 1; // entity라 반영이 안되어서 미리 뺀 값을 써줘야함
        if (slot.getHeadCount() == 0) {
            slot.setType(addUserDto.getType());
            slot.setGamePpp(addUserDto.getJoinUserPpp());
        } else {
            slot.setGamePpp((addUserDto.getJoinUserPpp() + slot.getGamePpp() * slot.getHeadCount()) / headCountResult);
        }
        slot.setHeadCount(headCountResult);
        if (slot.getMode() == Mode.BOTH) {
            slot.setMode(addUserDto.getMode());
        }
    }

    @Transactional
    public void removeUserInSlot(SlotRemoveUserDto removeUserDto) {
        Slot slot = slotRepository.findById(removeUserDto.getSlotId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer headCountResult = slot.getHeadCount() - 1; // entity라 반영이 안되어서 미리 뺀 값을 써줘야함
        if (headCountResult == 0) {
            slot.setType(null);
            slot.setGamePpp(null);
            slot.setMode(Mode.BOTH);
        } else {
            slot.setGamePpp((slot.getGamePpp() * slot.getHeadCount() - removeUserDto.getExitUserPpp()) / headCountResult);
        }
        slot.setHeadCount(headCountResult);
        //redisTemplate.opsForValue().set(Key.PENALTY_USER + removeUserDto.getUserId(), "true", 60, TimeUnit.SECONDS);
    }

    public SlotDto findSlotById(Integer slotId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new BusinessException("E0001"));
        return SlotDto.from(slot);
    }

    @Transactional
    public List<SlotStatusDto> findSlotsStatus(SlotFindStatusDto findDto) {
        LocalDateTime now = findDto.getCurrentTime();
        LocalDateTime todayStartTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        List<Slot> slots = slotRepository.findAllByTimeAfterOrderByTimeAsc(todayStartTime);

        User user = userRepository.findById(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime.now(), LocalDateTime.now()).orElse(null);
        Integer pppGap;
        if (season == null) {
            pppGap = 100;
        } else {
            pppGap = season.getPppGap();
        }
        CurrentMatch currentMatch = currentMatchRepository.findByUserAndIsDel(user, false).orElse(null);
        Integer userSlotId = currentMatch == null ? null : currentMatch.getSlot().getId();

        List<SlotStatusDto> slotStatusDtos = new ArrayList<>();
        for (Slot slot : slots) {
            SlotFilterDto filterDto = SlotFilterDto.builder()
                    .slot(SlotDto.from(slot))
                    .userSlotId(userSlotId)
                    .gameType(findDto.getType())
                    .userPpp(user.getPpp())
                    .pppGap(pppGap)
                    .userMode(findDto.getUserMode())
                    .build();
            SlotStatusType status = getStatus(filterDto);
            slotStatusDtos.add(SlotStatusDto.builder()
                    .slotId(slot.getId())
                    .headCount(slot.getHeadCount())
                    .time(slot.getTime())
                    .mode(slot.getMode())
                    .status(getStatus(filterDto))
                    .build()
            );
        }
        return slotStatusDtos;
    }

    public SlotDto findByTime(LocalDateTime time) {
        Slot slot = slotRepository.findByTime(time).orElse(null);
        SlotDto slotDto = slot == null ? null : SlotDto.from(slot);
        return slotDto;
    }

    public SlotDto findSlotBeforeTime(LocalDateTime now) {
        Slot slot = slotRepository.findFirstByTimeIsBeforeOrderByTimeDesc(now).orElse(null);
        SlotDto slotDto = slot == null ? null : SlotDto.from(slot);
        return slotDto;
    }

    public SlotStatusType getStatus(SlotFilterDto dto) {
        /* if currentTime > slotTime
            then status == close
           else if requestType != slotType
            then status == close
           else if abs(userPpp - gamePpp) > 100
            then status == close
           else if slotType == "double" and headCount == MAXCOUNT
            then status == close
         */
        Integer slotId = dto.getSlot().getId();
        Integer userSlotId = dto.getUserSlotId();
        GameType slotType = dto.getSlot().getType();
        LocalDateTime slotTime = dto.getSlot().getTime();
        GameType gameType = dto.getGameType();
        Integer gamePpp = dto.getSlot().getGamePpp();
        Integer userPpp = dto.getUserPpp();
        Integer headCount = dto.getSlot().getHeadCount();
        Integer pppGap = dto.getPppGap();
        Mode slotMode = dto.getSlot().getMode();
        LocalDateTime currentTime = LocalDateTime.now();
        Integer maxCount = 2;
        if (slotType != null && slotType.equals(GameType.DOUBLE)) {
            maxCount = 4;
        }
        SlotStatusType status = SlotStatusType.OPEN;
        if (currentTime.isAfter(slotTime)) {
            status = SlotStatusType.CLOSE;
        } else if (slotId.equals(userSlotId)) {
            status = SlotStatusType.MYTABLE;
        } else if (slotType != null && !gameType.equals(slotType)) {
            status = SlotStatusType.CLOSE;
        } else if (slotMode == Mode.RANK && gamePpp != null && Math.abs(userPpp - gamePpp) > pppGap) {
            status = SlotStatusType.CLOSE;
        } else if (headCount.equals(maxCount)) {
            status = SlotStatusType.CLOSE;
        } else if (dto.getUserMode() != null && !dto.getUserMode().equals(dto.getSlot().getMode()) && !dto.getSlot().getMode().equals(Mode.BOTH)) {
            status = SlotStatusType.CLOSE;
        }
        return status;
    }

    @Transactional
    public void createSlotByAdmin(SlotCreateRequestDto createDto) {
        slotRepository.save(Slot.builder()
                .tableId(createDto.getTableId())
                .time(createDto.getTime())
                .gamePpp(createDto.getGamePpp())
                .headCount(createDto.getHeadCount())
                .type(createDto.getType())
                .build());
    }

    @Transactional
    public void updateSlotByAdmin(SlotUpdateDto updateDto) {
        Slot slot = slotRepository.findById(updateDto.getSlotId()).orElseThrow(() -> new BusinessException("E0001"));
        slot.setGamePpp(updateDto.getGamePpp());
        slot.setHeadCount(updateDto.getHeadCount());
        slot.setType(updateDto.getType());
    }

    @Transactional
    public void deleteSlotByAdmin(SlotDeleteDto deleteDto) {
        Slot slot = slotRepository.findById(deleteDto.getSlotId()).orElseThrow(() -> new BusinessException("E0001"));
        slotRepository.delete(slot);
    }

    @Transactional
    public List<SlotDto> findSlotByAdmin(Pageable pageable) {
        Page<Slot> slots = slotRepository.findAllByOrderByIdDesc(pageable);
        List<SlotDto> slotDtos = slots.stream().map(SlotDto::from).collect(Collectors.toList());
        return slotDtos;
    }
}
