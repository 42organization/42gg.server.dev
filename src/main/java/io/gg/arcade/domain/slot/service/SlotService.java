package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.*;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SlotService{
    private final SlotRepository slotRepository;
    private final TeamRepository teamRepository;

    @Scheduled(cron = "0 0 0 1 * * *") // 나중에 global에 뺴야함
    @Transactional
    public void addTodaySlots() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 18; i++) {
            String team1Id = String.valueOf(UUID.randomUUID());
            String team2Id = String.valueOf(UUID.randomUUID());
            LocalDateTime test = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    15 + i / 6, (i * 10) % 60, 0);
            SlotAddRequestDto dto = SlotAddRequestDto.builder()
                    .team1Id(team1Id)
                    .team2Id(team2Id)
                    .time(test)
                    .build();
            addSlot(dto);
            teamRepository.save(
                    Team.builder()
                            .teamId(dto.getTeam1Id())
                            .teamPpp(0)
                            .headCount(0)
                            .build());
            teamRepository.save(
                    Team.builder()
                            .teamId(dto.getTeam2Id())
                            .teamPpp(0)
                            .headCount(0)
                            .build());
        }
    }

    @Transactional
    public void addSlot(SlotAddRequestDto dto) {
        slotRepository.save(
                Slot.builder()
                        .team1Id(dto.getTeam1Id())
                        .team2Id(dto.getTeam2Id())
                        .time(dto.getTime())
                        .headCount(0)
                        .build());
    }

    // 이 메서드가 호출된 뒤에 빈 혹은 찬 팀에 유저 추가
    @Transactional
    public void addUserInSlot(SlotModifyRequestDto slotDto) {
        Slot slot = slotRepository.getById(slotDto.getSlotId());
        if (slot.getHeadCount() == 0) {
            slot.setType(slotDto.getType());
            slot.setGamePpp(slotDto.getGamePpp());
        }
        slot.setHeadCount(slot.getHeadCount() + 1);
    }

    // 이 메서드가 호출된 뒤에 팀에 유저 제거
    @Transactional
    public void removeUserInSlot(SlotModifyRequestDto slotDto) {
        Slot slot = slotRepository.getById(slotDto.getSlotId());
        slot.setHeadCount(slot.getHeadCount() - 1);
        if (slot.getHeadCount() == 0) {
            slot.setType(null);
            slot.setGamePpp(null);
        }
    }

    @Transactional
    public SlotDto findSlotById(Integer slotId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(RuntimeException::new);
        return SlotDto.builder()
                .id(slot.getId())
                .headCount(slot.getHeadCount())
                .gamePpp(slot.getGamePpp())
                .team1Id(slot.getTeam1Id())
                .team2Id(slot.getTeam2Id())
                .time(slot.getTime())
                .type(slot.getType())
                .build();
    }

    private List<Slot> findByDate(SlotFindDto slotFindDto) {
        return slotRepository.findAllByCreatedDateIsAfter(slotFindDto.getLocalDateTime());
    }

    public List<SlotResponseDto> filterSlots(SlotFindDto slotFindDto) {
        List<Slot> slotList = findByDate(slotFindDto);
        List<SlotResponseDto> dtoList = new ArrayList<>();
        for (Slot slot : slotList) {
            dtoList.add(SlotResponseDto.builder()
                    .slotId(slot.getId())
                    .headCount(slot.getHeadCount())
                    .status(getStatus(slot.getType(), slotFindDto.getInquiringType(), slot.getHeadCount(), slot.getGamePpp(), slotFindDto.getCurrentUserPpp(), slotFindDto.getUserId()))
                    .build());
        }
        return dtoList;
    }

    private String getStatus(String slotType, String inquiringType, Integer headCount, Integer gamePpp, Integer ppp, Integer userId) {
        String status = isSlotAvailable(slotType, inquiringType, headCount, gamePpp, ppp) ? "open" : "close";
        if (isMyTable(userId) == true) {
            status = "myTable";
        }
        return status;
    }

    private Boolean isMyTable(Integer userId) {
        /*
        Boolean status = false;
        List<Team> teams = teamRepository.findByTeamId(slot.getTeam1Id());
        teams.addAll(teamRepository.findByTeamId(slot.getTeam2Id());
        for (Team team : teams) {
            if (team.getUser().getId().equals(userId)) {
                status = true;
                break;
            }
        }*/
        return false;
        //return status;
    }

    private Boolean isSlotAvailable(String slotType, String inquiringType, Integer headCount, Integer gamePpp, Integer ppp) {
        Integer diffLimit = 500;
        Boolean isAvailable = true;
        Integer maxHeadCount = 2;

        if (slotType != null && slotType.equals("double")) {
            maxHeadCount = 4;
        }

        if (headCount == 0) {
            isAvailable = true;
        } else if (slotType != null && !slotType.equals(inquiringType)) {
            isAvailable = false;
        } else if (headCount < maxHeadCount) {
            if (gamePpp != null && Math.abs(gamePpp - ppp) > diffLimit) {
                isAvailable = false;
            }
        } else if (headCount == maxHeadCount) {
            isAvailable = false;
        }
        return isAvailable;
    }
}
