package com.ebceu4.offlinechatsample.infrastructure.services;

import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;
import com.ebceu4.offlinechatsample.infrastructure.database.Db;
import com.ebceu4.offlinechatsample.infrastructure.database.DbTables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

@Singleton
public class OfflineChatService implements ChatService {
    public static final int GET_MESSAGES_PAGE_SIZE = 30;

    private Db db;
    private ChatNotifier chatNotifier;
    private Executor executor;
    private PublishSubject<ChatMessage> newMessagesStream = PublishSubject.create();

    @Inject
    public OfflineChatService(Db db, ChatNotifier chatNotifier){
        this.db = db;
        this.chatNotifier = chatNotifier;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public Observable<ChatMessage> getNewMessagesStream() {
        return newMessagesStream.observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ChatMessage> sendMessage(ChatMessage chatMessage){
        return Single.create(subscriber -> {

            try {
                ChatMessage insertedMessage = DbTables.ChatMessageTable.insert(db, chatMessage);
                subscriber.onSuccess(insertedMessage);
                newMessagesStream.onNext(insertedMessage);
                chatNotifier.notify(insertedMessage);
            }
            catch (Exception ex) {
                subscriber.onError(ex);
            }

        }).subscribeOn(Schedulers.from(executor)).observeOn(AndroidSchedulers.mainThread()).map(o -> (ChatMessage)o);
    }

    @Override
    public Single<Collection<ChatMessage>> getMessagesBefore(ChatMessage chatMessage){

        return Single.create(subscriber -> {

            Timestamp timestamp = null;
            if(chatMessage != null){
                timestamp = chatMessage.getTimestamp();
            }

            try{
                ArrayList<ChatMessage> result = DbTables.ChatMessageTable.select(db, GET_MESSAGES_PAGE_SIZE, timestamp);
                subscriber.onSuccess(result);
            }
            catch (Exception ex){
                subscriber.onError(ex);
            }

        }).subscribeOn(Schedulers.from(executor)).observeOn(AndroidSchedulers.mainThread())
                .map(o -> Collections.unmodifiableList((ArrayList<ChatMessage>) o));
    }

}
