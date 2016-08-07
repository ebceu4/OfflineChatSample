package com.ebceu4.offlinechatsample.infrastructure.services;

import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;

public interface ChatNotifier {
    void notify(ChatMessage chatMessage);
}
