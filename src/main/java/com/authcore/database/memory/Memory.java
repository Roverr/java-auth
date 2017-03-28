package com.authcore.database.memory;

import com.authcore.config.Config;
import com.authcore.database.Database;
import com.authcore.user.User;

import javax.json.*;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Memory extends Database {
    public ConcurrentMap<String, User> users;
    private File JsonDB;


    public Memory() {
        Config config = Config.getInstance();
        if (config.fileStoring) {
            FileFilter filter = new FileFilter() {
                public boolean accept(File pathname) {
                    if (!pathname.getName().contains(".json")) return false;
                    return true;
                }
            };
            File users = new File(config.fileName);
            if (filter.accept(users)) {
                this.JsonDB = users;
            }
        }
        this.users = new ConcurrentHashMap<String, User>();
    }

    private void SaveUsers() {
        JsonObjectBuilder db = Json.createObjectBuilder();
        for (Map.Entry<String, User> entry: this.users.entrySet()) {
            User u = entry.getValue();
            db.add(entry.getKey(), u.toIndicatorJSON());
        }
        JsonObject jsonDB = db.build();
        try {
            FileWriter file = new FileWriter(this.JsonDB);
            file.write(jsonDB.toString());
            file.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public Boolean InsertUser(User u) {
        this.users.putIfAbsent(u.email, u);
        this.SaveUsers();
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

    private JsonObject getUsersFromFile() throws IOException {
        FileReader fr = new FileReader(this.JsonDB);
        JsonObject jsonDB = Json.createReader(fr).readObject();
        fr.close();
        return jsonDB;
    }

    private void loadUsers(JsonObject jsonDB) {
        Set<String> keys = jsonDB.keySet();
        for (String key: keys) {
            JsonValue userString = jsonDB.get(key);
            JsonObject user = Json.createReader(new StringReader(userString.toString())).readObject();
            String email = key;
            try {
                String name = user.getString("name");
                String passwordHash = user.getString("passwordHash");
                String salt = user.getString("salt");
                User u = new User(email, name, passwordHash, salt);
                this.users.putIfAbsent(u.email, u);
            } catch (Exception e) {
                System.out.printf("\n%s email user has wrong structure in users.json\n", key);
            }
        }
    }

    public final void Initialize() {
        try {
            JsonObject jsonDB = getUsersFromFile();
            loadUsers(jsonDB);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
