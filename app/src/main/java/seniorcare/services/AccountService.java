package seniorcare.services;

import android.content.Context;

import java.util.List;

import seniorcare.db.sqlite.local.DbContext;
import seniorcare.models.User;

public class AccountService {
    private Context context;

    public AccountService(Context context) {
        this.context = context;
    }

    public boolean register(User user) {
        DbContext<User> userDbContext = new DbContext<>(context, new User());

        User searchUser = new User();
        searchUser.setName(user.getName());
        List<User> resultUsers =  userDbContext.searchData(searchUser);
        if (resultUsers.size() > 0) {
            return false;
        }
        userDbContext.insertData(user);
        return true;
    }

    public long login(User user) {
        DbContext<User> userDbContext = new DbContext<>(context, new User());
        return userDbContext.getId(user);
    }
}
