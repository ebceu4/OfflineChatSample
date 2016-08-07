package com.ebceu4.offlinechatsample;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.ebceu4.offlinechatsample.activities.chatRoom.ChatRoomActivity;
import com.ebceu4.offlinechatsample.infrastructure.androidServices.ChatBotService;
import com.ebceu4.offlinechatsample.infrastructure.dependencyInjection.AppModule;
import com.ebceu4.offlinechatsample.infrastructure.dependencyInjection.DaggerServiceInjector;
import com.ebceu4.offlinechatsample.infrastructure.dependencyInjection.ServiceInjector;

public abstract class InjectionApp extends Application {

    protected AppModule appModule;
    protected ServiceInjector serviceInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        appModule = createAppModule();
        serviceInjector = DaggerServiceInjector.builder().appModule(appModule).build();
    }

    public abstract AppModule createAppModule();

    public static class DI{

        public static void inject(Service service){
            if(service instanceof ChatBotService)
            {
                ((InjectionApp)service.getApplication()).serviceInjector.inject((ChatBotService)service);
            }
        }

        public static void inject(Activity activity){
            if(activity instanceof ChatRoomActivity)
            {
                ((InjectionApp)activity.getApplication()).serviceInjector.inject((ChatRoomActivity)activity);
            }
        }
    }
}
