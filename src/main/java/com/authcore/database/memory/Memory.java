package com.authcore.database.memory;

import com.authcore.config.Config;
import com.authcore.database.Database;
import com.authcore.logger.Logger;
import com.authcore.user.User;

import javax.json.*;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by imreleventeracz on 27/03/17.
 * Memory is an in-memory database implementation
 */
public class Memory extends Database {
    // Property to contain users
    public ConcurrentMap<String, User> users;
    // If file storing available File is stored here
    private File JsonDB;


    /**
     * Constructor creates a memory db instance and tries to
     * read users saved in json if it is possible
     */
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

    /**
     * SaveUsers is for saving users if file storing is on
     */
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
            Logger.Errorln(e);
        }
    }

    @Override
    public Boolean insertUser(User u) {
        this.users.putIfAbsent(u.email, u);
        Config config = Config.getInstance();
        if (config.fileStoring) {
            this.SaveUsers();
        }
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return this.users.get(email);
    }

    /**
     * Method for getting users from the json database file
     * @return JsonObject with the users
     * @throws IOException File reading can throw an exception
     */
    private JsonObject getUsersFromFile() throws IOException {
        FileReader fr = new FileReader(this.JsonDB);
        BufferedReader br = new BufferedReader(fr);
        JsonObject jsonDB = Json.createReader(br).readObject();
        br.close();
        fr.close();
        return jsonDB;
    }

    /**
     * Method for loading users from a jsonDB JsonObject into this.users
     * @param jsonDB JsonObject read from the file database
     */
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
                Logger.Debugln(String.format("%s email user has wrong structure in users.json", key));
            }
        }
    }

    /**
     * initialize is for initializing the file storing from a json file
     */
    public void initialize() {
        Config config = Config.getInstance();
        if (!config.fileStoring) {
            return;
        }
        try {
            JsonObject jsonDB = getUsersFromFile();
            loadUsers(jsonDB);
        } catch (Exception e) {
            Logger.Errorln(e);
        }
    }
}
