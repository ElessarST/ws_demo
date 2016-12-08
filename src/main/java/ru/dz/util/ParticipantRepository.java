package ru.dz.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ParticipantRepository {

	private Map<String, Event> activeSessions = new ConcurrentHashMap<>();

	public void add(String sessionId, Event event) {
		activeSessions.put(sessionId, event);
	}

	public Event getParticipant(String sessionId) {
		return activeSessions.get(sessionId);
	}

	public void removeParticipant(String sessionId) {
		activeSessions.remove(sessionId);
	}

	public Map<String, Event> getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(Map<String, Event> activeSessions) {
		this.activeSessions = activeSessions;
	}
}
