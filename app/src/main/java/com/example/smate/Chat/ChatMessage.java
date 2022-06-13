package com.example.smate.Chat;

public class ChatMessage {
    String senderPhone;
    String senderName;
    String message;
    String date;
    String time;

    public ChatMessage(String senderPhone, String senderName, String message, String date, String time) {
        this.senderPhone = senderPhone;
        this.senderName = senderName;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public ChatMessage() {
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
