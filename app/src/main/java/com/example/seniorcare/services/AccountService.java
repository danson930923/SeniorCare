package com.example.seniorcare.services;

import android.content.SharedPreferences;

import java.util.List;

import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.ContactInfo;
import com.example.seniorcare.models.ManagedUserPair;
import com.example.seniorcare.models.User;
import com.example.seniorcare.models.UserPassCode;

import org.apache.commons.text.RandomStringGenerator;
import org.joda.time.DateTime;

public class AccountService {
    private static final String USERNAME_TAG = "USERNAME";
    private static final String PASSWORD_TAG = "PASSWORD";
    private static final String TYPE_TAG = "TYPE";
    private static final String PASS_CODE_TAG = "PASS_CODE";

    private SqLiteLocalDbContext<User> userSqLiteLocalDbContext;
    private SqLiteLocalDbContext<UserPassCode> userPassCodeSqLiteLocalDbContext;
    private SqLiteLocalDbContext<ContactInfo> contactInfoSqLiteLocalDbContext;
    private SharedPreferences sharedPreferences;

    public AccountService(
            SqLiteLocalDbContext<User> userSqLiteLocalDbContext,
            SqLiteLocalDbContext<UserPassCode> userPassCodeSqLiteLocalDbContext,
            SqLiteLocalDbContext<ContactInfo> contactInfoSqLiteLocalDbContext,
            SharedPreferences sharedPreferences) {
        this.userSqLiteLocalDbContext = userSqLiteLocalDbContext;
        this.userPassCodeSqLiteLocalDbContext = userPassCodeSqLiteLocalDbContext;
        this.contactInfoSqLiteLocalDbContext = contactInfoSqLiteLocalDbContext;
        this.sharedPreferences = sharedPreferences;
    }

    public boolean register(User user, ContactInfo contactInfo) {
        User searchUser = new User();
        searchUser.setName(user.getName());
        List<User> resultUsers =  userSqLiteLocalDbContext.searchData(searchUser);
        if (resultUsers.size() > 0) {
            return false;
        }

        createNewUserInDb(user);
        UserPassCode passCode = createUserPassCodeInDb(user);
        contactInfoSqLiteLocalDbContext.insertData(contactInfo);
        saveToSharedPreference(user, passCode, contactInfo);

        return true;
    }

    private void createNewUserInDb(User user) {
        userSqLiteLocalDbContext.insertData(user);
    }

    private UserPassCode createUserPassCodeInDb(User user) {
        UserPassCode userPassCode = new UserPassCode();
        userPassCode.setUserId(user.getUserId());

//        char[][] range = {{'a','z'},{'0','9'}};
//        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
//                .withinRange(range).build();
//        String passCode = randomStringGenerator.generate(6);
//        userPassCode.setPassCode(passCode);

        String passCode = user.getUserId() + "_" + Math.round(Math.random() * 100);
        userPassCode.setPassCode(passCode);

        userPassCodeSqLiteLocalDbContext.insertData(userPassCode);
        return userPassCode;
    }

    private void createContactInfoInDb(ContactInfo contactInfo) {
        contactInfoSqLiteLocalDbContext.insertData(contactInfo);
    }

    private void saveToSharedPreference(User user, UserPassCode userPassCode, ContactInfo contactInfo) {
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(USERNAME_TAG, user.getName());
        prefEditor.putString(PASSWORD_TAG, user.getPassword());
        prefEditor.putString(TYPE_TAG, user.getType());
        prefEditor.putString(PASS_CODE_TAG, userPassCode.getPassCode());

        prefEditor.commit();
    }

    public boolean login(User user) {
        String dbId = userSqLiteLocalDbContext.getByPrimarykey(user).getUserId();
        if (dbId == null || dbId == "" || dbId == "null") {
            return false;
        }

        String userId = user.getUserId();
        UserPassCode userPassCodeSearch = new UserPassCode();
        userPassCodeSearch.setUserId(userId);
        UserPassCode userPassCode = userPassCodeSqLiteLocalDbContext.getByPrimarykey(userPassCodeSearch);

        ContactInfo contactInfoSearch = new ContactInfo();
        contactInfoSearch.setUserId(user.getUserId());
        ContactInfo contactInfo = contactInfoSqLiteLocalDbContext.getByPrimarykey(contactInfoSearch);
        saveToSharedPreference(user, userPassCode, contactInfo);

        return true;
    }

    public void  logout() {
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.remove(USERNAME_TAG);
        prefEditor.remove(PASSWORD_TAG);
        prefEditor.remove(TYPE_TAG);
        prefEditor.remove(PASS_CODE_TAG);

        prefEditor.commit();
    }

    public User getCurrentUser() {
        User user = new User();
        user.setName(sharedPreferences.getString(USERNAME_TAG, ""));
        user.setPassword(sharedPreferences.getString(PASSWORD_TAG, ""));
        user.setType(sharedPreferences.getString(TYPE_TAG, ""));
        return user;
    }

    public String getCurrentUserPassCode() {
        User user = getCurrentUser();
        UserPassCode userPassCode = new UserPassCode();
        userPassCode.setUserId(user.getUserId());

        userPassCode = userPassCodeSqLiteLocalDbContext.getByPrimarykey(userPassCode);

        return userPassCode.getPassCode();
    }

    public User getUserById(String userId) {
        return userSqLiteLocalDbContext.getByPrimarykey(userId);
    }
}
