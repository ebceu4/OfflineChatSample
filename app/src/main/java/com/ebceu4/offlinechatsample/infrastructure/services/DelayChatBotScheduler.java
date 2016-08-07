package com.ebceu4.offlinechatsample.infrastructure.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ebceu4.offlinechatsample.OfflineChatApplication;
import com.ebceu4.offlinechatsample.infrastructure.androidServices.ChatBotService;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DelayChatBotScheduler implements ChatBotScheduler {

    private int minDelay;
    private int maxDelay;
    private OfflineChatApplication application;
    private ChatBot chatBot;

    @Inject
    public DelayChatBotScheduler(int minDelay, int maxDelay, OfflineChatApplication application, ChatBot chatBot) {
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.application = application;
        this.chatBot = chatBot;
    }

    private int nextChatBotDelay() {
        return minDelay + new Random().nextInt(maxDelay - minDelay + 1);
    }

    @Override
    public void runBot() {
        chatBot.run();
    }

    @Override
    public void scheduleNextRun() {

        if (!application.isRunningWithoutActivities()) {
            AlarmManager alarm = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);

            alarm.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * nextChatBotDelay()),
                    PendingIntent.getService(application, 0, new Intent(application, ChatBotService.class), PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    @Override
    public void cancel()    {
        AlarmManager alarm = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel( PendingIntent.getService(application, 0, new Intent(application, ChatBotService.class), PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
