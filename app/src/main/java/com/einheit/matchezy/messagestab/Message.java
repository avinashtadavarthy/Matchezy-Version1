package com.einheit.matchezy.messagestab;

import java.util.Date;

public class Message {

    private String id;
    private String from;
    private String to;
    private String text;
    private String name;
    private String imageUrl;
    private long messageTime;
    private String fromName;
    private String toName;

    public Message() {
    }

    public Message(String from, String to, String text, String imageUrl,
                   String fromName, String toName) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.imageUrl = imageUrl;
        messageTime = new Date().getTime();
        this.fromName = fromName;
        this.toName = toName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFromName() {
        return fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}