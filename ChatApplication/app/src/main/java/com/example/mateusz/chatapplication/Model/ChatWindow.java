package com.example.mateusz.chatapplication.Model;

/**
 * Created by Mateusz on 30.12.2018.
 */

public class ChatWindow {
    private String sendingUser;
    private String recievingUser;
    private String message;

    public ChatWindow(String sendingUser, String recievingUser, String message) {
        this.sendingUser = sendingUser;
        this.recievingUser = recievingUser;
        this.message = message;
    }

    public ChatWindow() {
    }

    public String getSendingUser() {
        return sendingUser;
    }

    public void setSendingUser(String sendingUser) {
        this.sendingUser = sendingUser;
    }

    public String getRecievingUser() {
        return recievingUser;
    }

    public void setRecievingUser(String recievingUser) {
        this.recievingUser = recievingUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
