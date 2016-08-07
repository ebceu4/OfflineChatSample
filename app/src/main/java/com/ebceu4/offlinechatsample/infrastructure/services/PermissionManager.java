package com.ebceu4.offlinechatsample.infrastructure.services;


import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import rx.Single;

public interface PermissionManager {
    Single<Boolean> getPermission(String permission);
    void permissionRequestResult(String permission, Boolean result);
    void registerForPermissionProcessing(Activity activity);
    void unregisterFromPermissionProcessing(Activity activity);
}
