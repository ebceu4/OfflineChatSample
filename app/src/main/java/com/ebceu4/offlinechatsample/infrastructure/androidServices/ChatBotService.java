package com.ebceu4.offlinechatsample.infrastructure.androidServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ebceu4.offlinechatsample.InjectionApp;
import com.ebceu4.offlinechatsample.infrastructure.services.ChatBotScheduler;

import javax.inject.Inject;

public class ChatBotService extends Service {

    @Inject
    public ChatBotScheduler chatBotScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        InjectionApp.DI.inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        chatBotScheduler.runBot();

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        chatBotScheduler.scheduleNextRun();
    }
}
