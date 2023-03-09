package io.pp.arcade.v1.global.notification.slackbot;

public class SlackSendException extends Exception{
    public SlackSendException(String message) {
        super(message);
    }

    public SlackSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
