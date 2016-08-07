package com.ebceu4.offlinechatsample.infrastructure.dependencyInjection;

import com.ebceu4.offlinechatsample.OfflineChatApplication;
import com.ebceu4.offlinechatsample.infrastructure.database.Db;
import com.ebceu4.offlinechatsample.infrastructure.services.ChatBot;
import com.ebceu4.offlinechatsample.infrastructure.services.ChatBotScheduler;
import com.ebceu4.offlinechatsample.infrastructure.services.ChatNotifier;
import com.ebceu4.offlinechatsample.infrastructure.services.ChatService;
import com.ebceu4.offlinechatsample.infrastructure.services.DefaultLocationProvider;
import com.ebceu4.offlinechatsample.infrastructure.services.DefaultPermissionManager;
import com.ebceu4.offlinechatsample.infrastructure.services.DelayChatBotScheduler;
import com.ebceu4.offlinechatsample.infrastructure.services.LocationProvider;
import com.ebceu4.offlinechatsample.infrastructure.services.OfflineChatService;
import com.ebceu4.offlinechatsample.infrastructure.services.PermissionManager;
import com.ebceu4.offlinechatsample.infrastructure.services.SimpleChatBot;
import com.ebceu4.offlinechatsample.infrastructure.services.ToastChatNotifier;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private OfflineChatApplication application;

    public AppModule(OfflineChatApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Db provideDb() {
        return new Db(application);
    }

    @Provides
    @Singleton
    public ChatNotifier provideChatNotifier() {
        return new ToastChatNotifier(application);
    }

    @Provides
    @Singleton
    public ChatService provideChatService(Db db, ChatNotifier chatNotifier) {
        return new OfflineChatService(db, chatNotifier);
    }

    @Provides
    @Singleton
    public ChatBot provideChatBot(ChatService chatService, LocationProvider locationProvider) {
        return new SimpleChatBot(chatService, locationProvider);
    }

    @Provides
    @Singleton
    public ChatBotScheduler provideChatBotScheduler(ChatBot chatBot) {
        return new DelayChatBotScheduler(3, 5, application, chatBot);
    }

    @Provides
    @Singleton
    public PermissionManager providePermissionManager() {
        return new DefaultPermissionManager();
    }

    @Provides
    @Singleton
    public LocationProvider provideLocationProvider(PermissionManager permissionManager) {
        return new DefaultLocationProvider(application, permissionManager);
    }
}
