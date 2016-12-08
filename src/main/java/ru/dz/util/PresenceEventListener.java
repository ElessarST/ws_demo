package ru.dz.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Component
public class PresenceEventListener {

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	private static final String LOGIN = "/chat/chat.login";
	private static final String LOGOUT = "/chat/chat.logout";

	@EventListener
	private void handleSessionConnected(SessionConnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String username = headers.getUser().getName();

		Event loginEvent = new Event(username, true);
		messagingTemplate.convertAndSend(LOGIN, loginEvent);
		
		participantRepository.add(headers.getSessionId(), loginEvent);
	}
	
	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		
		Optional.ofNullable(participantRepository.getParticipant(event.getSessionId()))
				.ifPresent(login -> {
					messagingTemplate.convertAndSend(LOGOUT, new Event(login.getUsername(), false));
					participantRepository.removeParticipant(event.getSessionId());
				});
	}

}
