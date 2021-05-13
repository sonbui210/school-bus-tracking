package com.hoanganhbk.schoolBus.helper;

import com.hoanganhbk.schoolBus.api.bus.PositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketHelper {

    private final SimpMessagingTemplate messagingTemplate;

    public void pushBusEvent(PositionDto positionDto) {
        messagingTemplate.convertAndSend("/ws-bus/" + positionDto.getBusId(),  positionDto);
    }
}
