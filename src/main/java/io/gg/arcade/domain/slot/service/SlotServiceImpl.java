package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.rank.repository.RankRepository;
import io.gg.arcade.domain.slot.dto.SlotFindDto;
import io.gg.arcade.domain.slot.dto.SlotResponseDto;
import io.gg.arcade.domain.slot.dto.SlotRequestDto;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.team.repository.TeamRepository;
import io.gg.arcade.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository;
    private final TeamRepository teamRepository;

    @Override
    @Scheduled(cron = "0 0 0 1 * * *") // 나중에 global에 뺴야함
    public void addTodaySlots() {

        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 18; i++) {
            addSlot(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    15 + i / 6, (i * 10) % 60, 0));
        }
    }

    @Override
    public Slot addSlot(LocalDateTime time) {
        Slot slot = Slot.builder()
                .team1Id(String.valueOf(UUID.randomUUID()))
                .team2Id(String.valueOf(UUID.randomUUID()))
                .time(time)
                .headCount(0)
                .build();
        return slotRepository.save(slot);
    }

    @Override // 이 메서드가 호출된 뒤에 빈 혹은 찬 팀에 유저 추가
    public void addUserInSlot(SlotRequestDto slotDto) {
        Slot slot = slotRepository.getById(slotDto.getSlotId());
        if (slot.getHeadCount() == 0) {
            slot.setType(slotDto.getType());
            slot.setGamePpp(slotDto.getGamePpp());
        }
        slot.setHeadCount(slot.getHeadCount() + 1);
    }

    @Override // 이 메서드가 호출된 뒤에 팀에 유저 제거
    public void removeUserInSlot(SlotRequestDto slotDto) {
        Slot slot = slotRepository.getById(slotDto.getSlotId());
        slot.setHeadCount(slot.getHeadCount() - 1);
        if (slot.getHeadCount() == 0) {
            slot.setType(null);
            slot.setGamePpp(null);
        }
    }

    private List<Slot> findByDate(SlotFindDto slotFindDto) {
        return slotRepository.findAllByCreatedDateAfter(slotFindDto.getLocalDateTime());
    }

    public List<SlotResponseDto> filterSlots(SlotFindDto slotFindDto) {
        List<Slot> slotList = findByDate(slotFindDto);
        List<SlotResponseDto> dtoList = new ArrayList<>();
        for (Slot slot : slotList) {
            dtoList.add(SlotResponseDto.builder()
                    .slotId(slot.getId())
                    .headCount(slot.getHeadCount())
                    .status(getStatus(slotFindDto.getInquiringType(), slot.getHeadCount(), slot.getGamePpp(), slotFindDto.getCurrentUserPpp(), slotFindDto.getUserId()))
                    .build());
        }
        return dtoList;
    }

    private String getStatus(String inquiringType, Integer headCount, Integer gamePpp, Integer ppp, Integer userId) {
        String status = isSlotAvailable(inquiringType, headCount, gamePpp, ppp) ? "open" : "close";
        if (isMyTable(userId, status) == true) {
            status = "myTable";
        }
        return status;
    }

    private Boolean isMyTable(Integer userId, String status) {
        Boolean status = false;
        List<Team> teams = teamRepository.findByTeamId(slot.getTeam1Id());
        teams.addAll(teamRepository.findByTeamId(slot.getTeam2Id());
        for (Team team : teams) {
            if (team.getUser().getId().equals(userId)) {
                status = true;
                break;
            }
        }
        return status;
    }

    private Boolean isSlotAvailable(String inquiringType, Integer headCount, Integer gamePpp, Integer ppp) {
        Integer diffLimit = 500;
        Boolean isAvailable = true;

        if (headCount == 0) {
            isAvailable = true;
        } else if (inquiringType.equals("single")) {
            if (headCount == 1) {
                if (gamePpp != null && Math.abs(gamePpp - ppp) > diffLimit) {
                    isAvailable = false;
                }
            } else if (headCount == 2) {
                isAvailable = false;
            }
        } else if (inquiringType.equals("double")) {
            if (headCount < 4) {
                if (gamePpp != null && Math.abs(gamePpp - ppp) > diffLimit) {
                    isAvailable = false;
                }
            } else if (headCount == 4) {
                isAvailable = false;
            }
        }
        return isAvailable;
    }

//    private String getStatus(Boolean isSingle, Slot slot, Integer ppp, Integer userId) {
//        String type = slot.getType();
//        Integer headCount = slot.getHeadCount();
//        String status = "open";
//        Integer diffLimit = 500;
//
//        if (type.equals("single")) {
//            if (headCount == 1) {
//                if (slot.getGamePpp() != null && Math.abs(slot.getGamePpp() - ppp) > diffLimit) {
//                    status = "close";
//                }
//            } else if (headCount == 2) {
//                status = "close";
//            }
//        } else if (type.equals("double")) {
//            if (0 < headCount && headCount < 4) {
//                if (slot.getGamePpp() != null && Math.abs(slot.getGamePpp() - ppp) > diffLimit) {
//                    status = "close";
//                }
//            } else if (headCount == 4) {
//                status = "close";
//            }
//        }
//    }
}
