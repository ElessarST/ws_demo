package ru.dz.util;

public class Event {

	private String username;
	private boolean online;
	private boolean me;

	public Event(String username, boolean online) {
		this.username = username;
		this.online = online;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isMe() {
		return me;
	}

	public void setMe(boolean me) {
		this.me = me;
	}
}
