package com.ebceu4.offlinechatsample;

import com.ebceu4.offlinechatsample.infrastructure.dependencyInjection.AppModule;
import com.ebceu4.offlinechatsample.infrastructure.services.ChatBotScheduler;
import javax.inject.Inject;

public class OfflineChatApplication extends InjectionApp {

    @Inject
    public ChatBotScheduler chatBotScheduler;

    private static int activitiesCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        serviceInjector.inject(this);
    }

    @Override
    public AppModule createAppModule() {
        return new AppModule(this);
    }

    public void activityCreated() {
        activitiesCount++;
        chatBotScheduler.scheduleNextRun();
    }

    public void activityDestroyed() {
        activitiesCount--;
    }

    public boolean isRunningWithoutActivities() {
        return activitiesCount == 0;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        chatBotScheduler.cancel();
    }
}
