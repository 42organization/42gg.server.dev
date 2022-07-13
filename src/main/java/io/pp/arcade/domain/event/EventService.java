package io.pp.arcade.domain.event;

import io.pp.arcade.domain.event.dto.EventUserDto;
import io.pp.arcade.domain.event.dto.FindEventDto;
import io.pp.arcade.domain.event.dto.SaveEventDto;
import io.pp.arcade.domain.event.dto.SaveEventUserDto;
import io.pp.arcade.domain.event.entity.EventUser;
import io.pp.arcade.domain.event.entity.PingPongEvent;
import io.pp.arcade.domain.event.repository.EventUserRepository;
import io.pp.arcade.domain.event.repository.PingPongEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventUserRepository eventUserRepository;
    private final PingPongEventRepository pingPongEventRepository;

    @Transactional
    public void saveEventUser(SaveEventUserDto saveDto) {
        EventUser eventUser = EventUser.builder()
                .intraId(saveDto.getIntraId())
                .eventName(saveDto.getEventName())
                .build();
        eventUserRepository.save(eventUser);
    }

//    @Transactional
//    public void savePingPongEvent(SaveEventDto saveDto) {
//        PingPongEvent pingPongEvent = PingPongEvent.builder()
//                .eventName(saveDto.getEventName())
//                .content(saveDto.getContent())
//                .currentEvent(false)
//                .build();
//        pingPongEventRepository.save(pingPongEvent);
//    }
//
    @Transactional
    public List<EventUserDto> findByEventName(FindEventDto findDto) {
        List<EventUser> eventUsers = eventUserRepository.findByEventName(findDto.getEventName()).orElse(null);
        List<EventUserDto> eventUserDtos = null;
        if (eventUsers != null) {
            eventUserDtos = eventUsers.stream().map(EventUserDto::from).collect(Collectors.toList());
        }
        return eventUserDtos;
    }
//
//    @Transactional
//    public List<PingPongEvent> findCurrentEvent() {
//        return pingPongEventRepository.findByCurrentEvent(true).orElse(null);
//    }
}
