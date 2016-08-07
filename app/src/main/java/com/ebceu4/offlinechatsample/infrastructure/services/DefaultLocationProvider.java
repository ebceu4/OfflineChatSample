package com.ebceu4.offlinechatsample.infrastructure.services;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import com.ebceu4.offlinechatsample.OfflineChatApplication;

import javax.inject.Inject;

import rx.Single;

public class DefaultLocationProvider implements LocationProvider {

    private OfflineChatApplication application;
    private PermissionManager permissionManager;
    private Location location;

    @Inject
    public DefaultLocationProvider(OfflineChatApplication application, PermissionManager permissionManager)
    {
        this.application = application;
        this.permissionManager = permissionManager;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public Single<Location> getLocation() {

        return permissionManager.getPermission(Manifest.permission.ACCESS_COARSE_LOCATION).map(isGranted -> {

            if (isGranted) {

                try {
                    LocationManager locationManager = (LocationManager) application
                            .getSystemService(Context.LOCATION_SERVICE);

                    Boolean isGPSEnabled = locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (isGPSEnabled) {
                        Location lastKnownLocation = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(lastKnownLocation != null)
                            location = lastKnownLocation;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return location;
            }

            return location;
        });
    }
}
