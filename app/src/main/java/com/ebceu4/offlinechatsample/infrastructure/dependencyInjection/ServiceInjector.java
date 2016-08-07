package com.ebceu4.offlinechatsample.infrastructure.dependencyInjection;

import com.ebceu4.offlinechatsample.OfflineChatApplication;
import com.ebceu4.offlinechatsample.activities.chatRoom.ChatRoomActivity;
import com.ebceu4.offlinechatsample.infrastructure.androidServices.ChatBotService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface ServiceInjector {
    void inject(OfflineChatApplication application);
    void inject(ChatRoomActivity activity);
    void inject(ChatBotService service);
}
