package com.example.seniorcare.services;

import android.content.Intent;
import android.net.Uri;

import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.ContactInfo;
import com.example.seniorcare.models.User;

public class ContactInfoService {
    private SqLiteLocalDbContext<ContactInfo> contactInfoSqLiteLocalDbContext;

    public ContactInfoService(
            SqLiteLocalDbContext<ContactInfo> contactInfoSqLiteLocalDbContext
    ) {
        this.contactInfoSqLiteLocalDbContext = contactInfoSqLiteLocalDbContext;
    }

    public String getPhoneNumber(User user) {
        ContactInfo contactInfoSearch = new ContactInfo();
        contactInfoSearch.setUserId(user.getUserId());
        ContactInfo contactInfo = contactInfoSqLiteLocalDbContext.searchData(contactInfoSearch).get(0);
        return  contactInfo.getPhoneNumber();
    }

    public Intent getCallIntent(User user) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + getPhoneNumber(user)));
        return callIntent;
    }
}
