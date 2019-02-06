package com.example.mateusz.chatapplication.Notification;

/**
 * Created by Mateusz on 03.02.2019.
 * Class combines notification data and Receiving Person id.
 */

public class Sender {
    public Data notification;
    public String to;

    public Sender(Data notification, String to) {
        this.notification = notification;
        this.to = to;
    }

}
