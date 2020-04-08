package com.example.seniorcare.services;

import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.LocationInfo;
import com.example.seniorcare.models.User;

public class LocationService  {
    private SqLiteLocalDbContext<LocationInfo> locationInfoSqLiteLocalDbContext;

    public LocationService(
            SqLiteLocalDbContext<LocationInfo> locationInfoSqLiteLocalDbContext
    ) {
        this.locationInfoSqLiteLocalDbContext = locationInfoSqLiteLocalDbContext;
    }

    public void updateLocation(LocationInfo locationInfo) {
        locationInfoSqLiteLocalDbContext.updateData(locationInfo);
    }
}
