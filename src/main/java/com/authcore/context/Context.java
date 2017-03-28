package com.authcore.context;

import com.authcore.database.Database;
import com.authcore.database.memory.Memory;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Context {
    private static Context instance = null;
    public Database connection;
    protected Context(Database conn) {
        this.connection = conn;
    }
    public static Context getInstance() {
        if(instance == null) {
            Database db = new Memory();
            db.Initialize();
            instance = new Context(db);
        }
        return instance;
    }
}
