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
     * insertUser inserts a user into the database implementation
     * @param u User instance
     * @return Gives back true if storing was successful, false if not
     */
    abstract public Boolean insertUser(User u);

    /**
     * getUserByEmail gets a user by its email from the database implementation
     * @param email Email to search for
     * @return User instance
     */
    abstract public User getUserByEmail(String email);

    /**
     * initialize is for loading pre-requirements
     * for the database implementation
     */
    abstract public void initialize();
}
