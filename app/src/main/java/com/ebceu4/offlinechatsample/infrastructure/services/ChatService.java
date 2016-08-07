package com.ebceu4.offlinechatsample.infrastructure.services;

import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observable;
import rx.Single;

public interface ChatService {
    Observable<ChatMessage> getNewMessagesStream();
    Single<ChatMessage> sendMessage(ChatMessage chatMessage);
    Single<Collection<ChatMessage>> getMessagesBefore(ChatMessage chatMessage);
}
