package com.ebceu4.offlinechatsample.infrastructure.services;

public interface ChatBotScheduler {
    void scheduleNextRun();
    void cancel();
    void runBot();
}
