package com.authcore.database;

import com.authcore.user.User;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.FileFilter;
import java.io.FileWriter;

/**
 * Created by imreleventeracz on 27/03/17.
 */
abstract public class Database {
    abstract public Boolean InsertUser(User u);
    abstract public Boolean DeleteUser(User u);
    abstract public User GetUserByEmail(String email);
    abstract public void Initialize();
}
