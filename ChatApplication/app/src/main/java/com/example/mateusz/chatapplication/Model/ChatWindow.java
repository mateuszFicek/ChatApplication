package com.example.mateusz.chatapplication.Model;

/**
 * Created by Mateusz on 30.12.2018.
 */

public class ChatWindow {
    private String sendingUser;
    private String recievingUser;
    private String message;
    private boolean isseen;

    public ChatWindow(String sendingUser, String recievingUser, String message, boolean isseen) {
        this.sendingUser = sendingUser;
        this.recievingUser = recievingUser;
        this.message = message;
        this.isseen = isseen;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
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
