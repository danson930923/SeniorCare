package com.example.seniorcare.services;

import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.ManagedUserPair;
import com.example.seniorcare.models.User;
import com.example.seniorcare.models.UserPassCode;

import java.util.List;
import java.util.stream.Collectors;

public class ManageUserService {
    private SqLiteLocalDbContext<ManagedUserPair> managedUserPairSqLiteLocalDbContext;
    private SqLiteLocalDbContext<UserPassCode> userPassCodeSqLiteLocalDbContext;
    private SqLiteLocalDbContext<User> userSqLiteLocalDbContext;

    public ManageUserService(
            SqLiteLocalDbContext<ManagedUserPair> managedUserPairSqLiteLocalDbContext,
            SqLiteLocalDbContext<UserPassCode> userPassCodeSqLiteLocalDbContext,
            SqLiteLocalDbContext<User> userSqLiteLocalDbContext
    ){
        this.managedUserPairSqLiteLocalDbContext = managedUserPairSqLiteLocalDbContext;
        this.userPassCodeSqLiteLocalDbContext = userPassCodeSqLiteLocalDbContext;
        this.userSqLiteLocalDbContext = userSqLiteLocalDbContext;
    }

    public List<String> getManagedUserIds(User user) {
        ManagedUserPair managedUserPairSearch = new ManagedUserPair();
        managedUserPairSearch.setManagerId(user.getUserId());
        return managedUserPairSqLiteLocalDbContext.searchData(managedUserPairSearch).stream().map(managedUserPair -> managedUserPair.getManagedId()).collect(Collectors.toList());
    }

    public User getManagerUser(User user) {
        ManagedUserPair managedUserPair = new ManagedUserPair();
        managedUserPair.setManagerId(user.getUserId());
        String userId = managedUserPairSqLiteLocalDbContext.searchData(managedUserPair).get(0).getManagerId();
        User managedUser = userSqLiteLocalDbContext.getByPrimarykey(userId);

        return managedUser;
    }

    public boolean addManagedUser(User manager, UserPassCode userPassCode) {
        List<UserPassCode> searchResults = userPassCodeSqLiteLocalDbContext.searchData(userPassCode);
        if (searchResults.size() <= 0) {
            return false;
        }

        userPassCode = searchResults.get(0);
        ManagedUserPair managedUserPair = new ManagedUserPair();
        managedUserPair.setManagerId(manager.getUserId());
        managedUserPair.setManagedId(userPassCode.getUserId());

        if (managedUserPairSqLiteLocalDbContext.searchData(managedUserPair).size() > 0) {
            return true;
        }

        managedUserPairSqLiteLocalDbContext.insertData(managedUserPair);

        return true;
    }
}
