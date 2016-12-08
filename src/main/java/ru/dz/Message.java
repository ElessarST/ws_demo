package ru.dz;

/**
 * Created by Aydar Farrakhov on 27.11.16.
 */
public class Message {

    private String username;

    private String text;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(String username, String text) {
        this.username = username;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
