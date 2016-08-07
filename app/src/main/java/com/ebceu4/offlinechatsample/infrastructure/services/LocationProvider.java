package com.ebceu4.offlinechatsample.infrastructure.services;

import android.location.Location;

import rx.Single;

public interface LocationProvider {
    Single<Location> getLocation();
}
