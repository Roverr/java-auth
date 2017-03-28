package com.authcore.database.memory;

import com.authcore.database.Database;
import com.authcore.user.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Memory extends Database {
    public ConcurrentMap<String, User> users;

    public Memory() {
        this.users = new ConcurrentHashMap<String, User>();
    }

    @Override
    public Boolean InsertUser(User u) {
        this.users.putIfAbsent(u.email, u);
        return true;
    }

    @Override
    public Boolean DeleteUser(User u) {
        this.users.remove(u);
        return true;
    }

    @Override
    public User GetUserByEmail(String email) {
        return this.users.get(email);
    }
}
