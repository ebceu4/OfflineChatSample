package com.ebceu4.offlinechatsample.infrastructure.dto;

import java.sql.Timestamp;

public class ChatMessage {

    private long id;
    public long getId() {
        return id;
    }

    private boolean isSentByUser;
    public boolean getIsSentByUser() {
        return isSentByUser;
    }

    private Timestamp timestamp;
    public Timestamp getTimestamp() {
        return timestamp;
    }

    private String text;
    public String getText() {
        return text;
    }

    public ChatMessage(String text, boolean isSentByUser) {
        this.text=text;
        this.isSentByUser=isSentByUser;
    }

    public ChatMessage(long id, String text, Timestamp timestamp, boolean isSentByUser) {
        this.id = id;
        this.text=text;
        this.timestamp = timestamp;
        this.isSentByUser = isSentByUser;
    }


}
