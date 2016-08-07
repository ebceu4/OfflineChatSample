package com.ebceu4.offlinechatsample.infrastructure.services;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import org.antlr.v4.runtime.misc.Tuple2;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import rx.Single;
import rx.subjects.PublishSubject;


public class DefaultPermissionManager implements PermissionManager {

    private Activity permissionProcessor;
    private LinkedHashSet<String> requestedPermissions = new LinkedHashSet<>();
    private HashSet<String> grantedPermissions = new HashSet<>();
    private String pendingPermission;
    private PublishSubject<RequestResult> permissions = PublishSubject.create();

    private class RequestResult{
        private String permission;
        private Boolean isGranted;

        public RequestResult(String permission, Boolean isGranted){
            this.permission = permission;

            this.isGranted = isGranted;
        }

        public String getPermission() {
            return permission;
        }

        public Boolean getGranted() {
            return isGranted;
        }
    }

    private void tryToProcessPermission()
    {
        if(requestedPermissions.size() <= 0)
            return;

        if(permissionProcessor == null)
            return;

        Iterator<String> iterator = requestedPermissions.iterator();
        pendingPermission = iterator.next();
        iterator.remove();

        if(ActivityCompat.checkSelfPermission(permissionProcessor , pendingPermission) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(permissionProcessor, new String[]{pendingPermission},  0);
            }
        }
        else {
            permissionRequestResult(pendingPermission, true);
        }
    }

    @Override
    public Single<Boolean> getPermission(String permission) {

        if(grantedPermissions.contains(permission))
            return Single.just(true);

        if(!requestedPermissions.contains(permission))
        {
            requestedPermissions.add(permission);
            tryToProcessPermission();
        }

        return permissions.filter(x -> x.getPermission().equals(permission)).map(RequestResult::getGranted).toSingle();
    }

    @Override
    public void permissionRequestResult(String permission, Boolean result) {
        if(!grantedPermissions.contains(permission) && result)
        {
            grantedPermissions.add(permission);
        }

        permissions.onNext(new RequestResult(permission, result));
        pendingPermission = null;
        tryToProcessPermission();
    }

    @Override
    public void registerForPermissionProcessing(Activity activity) {
        if(permissionProcessor != null && permissionProcessor != activity)
        {
            throw new InvalidParameterException("Permission processor is already registered");
        }

        permissionProcessor = activity;
        tryToProcessPermission();
    }

    @Override
    public void unregisterFromPermissionProcessing(Activity activity) {
        permissionProcessor = null;
    }
}
