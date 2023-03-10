package io.pp.arcade.v1.global.scheduler;

import io.pp.arcade.v1.admin.slot.SlotManagement;
import io.pp.arcade.v1.admin.slot.service.SlotAdminService;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.v1.domain.slot.SlotService;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.util.NotiGenerater;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;

//@Scheduled(cron = "0 */" + intervalTime + " " + startTime + "-" + endTime + " * * *", zone = "Asia/Seoul") // 초 분 시 일 월 년 요일
@Component
public class CurrentMatchUpdater extends AbstractScheduler {
    private final SlotService slotService;
    private final NotiGenerater notiGenerater;
    private final CurrentMatchService currentMatchService;
    private final CurrentMatchRepository currentMatchRepository;
    private final SlotAdminService slotAdminService;

    public CurrentMatchUpdater(SlotService slotService, NotiGenerater notiGenerater,
                               CurrentMatchService currentMatchService, CurrentMatchRepository currentMatchRepository, SlotAdminService slotAdminService) {
        this.slotService = slotService;
        this.notiGenerater = notiGenerater;
        this.currentMatchService = currentMatchService;
        this.currentMatchRepository = currentMatchRepository;
        this.slotAdminService = slotAdminService;
        this.setCron("0 */1 * * * *");
    }

    public void updateIsImminent() throws MessagingException {
        SlotManagement nowSlotPolicy = slotAdminService.getNowSlotPolicy();
        LocalDateTime now = LocalDateTime.now().plusSeconds(1);
        now = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0);
        now = now.plusMinutes(nowSlotPolicy.getOpenMinute());
        update(now);
    }

    public void updateIsImminent(LocalDateTime time) throws MessagingException {
        update(time);
    }

    private void update(LocalDateTime now) throws MessagingException {
        SlotDto slot = slotService.findByTime(now);
        if (slot == null) {
            return;
        }
        if (currentMatchAlreadyUpdated(slot))
            return;
        Integer maxHeadCount = GameType.SINGLE.equals(slot.getType()) ? 2 : 4;
        if (maxHeadCount.equals(slot.getHeadCount())) {
            CurrentMatchModifyDto modifyDto = CurrentMatchModifyDto.builder()
                    .slot(slot)
                    .isMatched(true)
                    .matchImminent(true)
                    .build();
            currentMatchService.modifyCurrentMatch(modifyDto);
            notiGenerater.addMatchNotisBySlot(slot);
        }
    }

    private boolean currentMatchAlreadyUpdated(SlotDto slot) {
        List<CurrentMatch> currentMatches = currentMatchRepository.findAllBySlotId(slot.getId());
        for (CurrentMatch currentMatch : currentMatches) {
            if (currentMatch.getMatchImminent())
                return true;
        }
        return false;
    }

    @Override
    public Runnable runnable() {
        return () -> {
            try {
                updateIsImminent();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };
    }
}
