package com.ebceu4.offlinechatsample.activities.chatRoom;

import android.databinding.ObservableField;

import com.ebceu4.offlinechatsample.general.LoadMoreObservableArrayList;
import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;
import com.ebceu4.offlinechatsample.infrastructure.services.ChatService;

import javax.inject.Inject;

public class ChatRoomViewModel {

    private LoadMoreObservableArrayList<ChatMessage> messages;
    private ChatService chatService;

    public LoadMoreObservableArrayList<ChatMessage> getMessages() {
        return messages;
    }

    private ObservableField<String> text = new ObservableField<>();
    public ObservableField<String> getText() {
        return text;
    }

    private ObservableField<Boolean> isSending = new ObservableField<>(false);
    public ObservableField<Boolean> getIsSending() {
        return isSending;
    }

    @Inject
    public ChatRoomViewModel(ChatService chatService) {
        this.chatService = chatService;

        this.messages = new LoadMoreObservableArrayList<>(collection -> {
            ChatMessage topmostMessage = null;
            if (messages.size() > 0)
                topmostMessage = messages.get(0);

            return chatService.getMessagesBefore(topmostMessage)
                    .doOnSuccess(chatMessages -> collection.addAll(0, chatMessages))
                    .map(o -> o);
        });

        chatService.getNewMessagesStream().subscribe(chatMessage -> {
            messages.add(chatMessage);
        });
    }

    public boolean sendMessage() {
        if(isSending.get())
            return false;

        isSending.set(true);

        chatService.sendMessage(new ChatMessage(text.get(), true)).subscribe(chatMessage -> {
            text.set("");
            isSending.set(false);
        });

        return true;
    }
}
