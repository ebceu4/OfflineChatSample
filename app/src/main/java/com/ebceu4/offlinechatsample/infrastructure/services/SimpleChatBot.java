package com.ebceu4.offlinechatsample.infrastructure.services;

import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SimpleChatBot implements ChatBot {

    private ChatService chatService;
    private LocationProvider locationProvider;

    @Inject
    public SimpleChatBot(ChatService chatService, LocationProvider locationProvider) {

        this.chatService = chatService;
        this.locationProvider = locationProvider;
    }

    @Override
    public void run() {

        locationProvider.getLocation().subscribe(location -> {

            String message = "Can't find your location";

            if(location != null) {
                message = Double.toString(location.getLatitude()) + ":" + Double.toString(location.getLongitude());
            }

            chatService.sendMessage(new ChatMessage(message, false)).subscribe(chatMessage -> {

            }, error -> {

            });
        });
    }
}
