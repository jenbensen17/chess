package client.websocket;

import websocket.messages.Notification;

public class NotificationHandler {
    void notify(Notification notification) {
        System.out.println("NOTIFICATION: "+ notification.toString());
    }
}
