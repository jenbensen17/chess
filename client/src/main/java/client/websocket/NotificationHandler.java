package client.websocket;

import websocket.messages.Notification;

public class NotificationHandler {
    void notify(Notification notification) {
        System.out.println(notification.toString());
    }
}
