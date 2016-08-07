package com.ebceu4.offlinechatsample.infrastructure.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.ebceu4.offlinechatsample.OfflineChatApplication;
import com.ebceu4.offlinechatsample.R;
import com.ebceu4.offlinechatsample.activities.chatRoom.ChatRoomActivity;
import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

public class ToastChatNotifier implements ChatNotifier {

    private OfflineChatApplication application;

    @Inject
    public ToastChatNotifier(OfflineChatApplication application) {
        this.application = application;
    }

    @Override
    public void notify(ChatMessage chatMessage) {

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM hh:mm:ss", Locale.getDefault());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(application)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setContentTitle("New message: " + dateformat.format(chatMessage.getTimestamp().getTime()))
                        .setContentText(chatMessage.getText())
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(application, ChatRoomActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(application);
        stackBuilder.addParentStack(ChatRoomActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
