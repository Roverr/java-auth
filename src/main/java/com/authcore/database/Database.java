package com.authcore.database;

import com.authcore.user.User;

/**
 * Created by imreleventeracz on 27/03/17.
 */
abstract public class Database {
    abstract public Boolean InsertUser(User u);
    abstract public Boolean DeleteUser(User u);
    abstract public User GetUserByEmail(String email);
}
