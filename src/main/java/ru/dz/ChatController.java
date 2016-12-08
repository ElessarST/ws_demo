package ru.dz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import ru.dz.util.Event;
import ru.dz.util.ParticipantRepository;

import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Aydar Farrakhov on 27.11.16.
 */
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ParticipantRepository participantRepository;


    @SubscribeMapping("/chat.participants")
    public Collection<Event> participants(Principal principal) {
        return participantRepository.getActiveSessions().values().stream()
                .map(p-> {
                    p.setMe(principal.getName().equals(p.getUsername()));
                    return p;
                })
                .collect(Collectors.toList());
    }

    @MessageMapping("/chat.all")
    @SendTo("/chat/all")
    public Message all(Message message, Principal principal) throws Exception {
        return new Message(principal.getName(), message.getText());
    }

    @MessageMapping("/chat.{username}")
    public void privateMessage(@Payload Message message, @DestinationVariable("username") String username, Principal principal) {

        Message send = new Message(principal.getName(), message.getText());

        simpMessagingTemplate.convertAndSend("/user/" + username + "/exchange/amq.direct/chat.message", send);
    }

}