package com.authcore.user;

import com.authcore.context.Context;
import org.mindrot.BCrypt;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class User {
    public String email;
    public String name;
    private String password;
    private String salt;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    };

    public User(String email, String name, String password, String salt) {
        this.email=email;
        this.password=password;
        this.salt=salt;
        this.name = name;
    }

    public static User Create(String email, String name, String password) {
        Context server = Context.getInstance();
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        User u = new User(email, name, hashedPassword, salt);
        server.connection.InsertUser(u);
        return u;
    }

    public JsonObject toIndicatorJSON() {
        return Json.createObjectBuilder()
                .add("name", this.name)
                .add("passwordHash", this.password)
                .add("salt", this.salt)
                .build();
    }

    public Boolean validatePassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
