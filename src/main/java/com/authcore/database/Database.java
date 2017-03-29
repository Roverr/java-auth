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
    /**
     * InsertUser inserts a user into the database implementation
     * @param u User instance
     * @return Gives back true if storing was successful, false if not
     */
    abstract public Boolean InsertUser(User u);

    /**
     * GetUserByEmail gets a user by its email from the database implementation
     * @param email Email to search for
     * @return User instance
     */
    abstract public User GetUserByEmail(String email);

    /**
     * Initialize is for loading pre-requirements
     * for the database implementation
     */
    abstract public void Initialize();
}
