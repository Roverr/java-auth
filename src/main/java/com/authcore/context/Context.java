package com.authcore.context;

import com.authcore.database.Database;
import com.authcore.database.memory.Memory;

/**
 * Created by imreleventeracz on 27/03/17.
 * Context is a singleton object for server context
 */
public class Context {
    private static Context instance = null;
    public Database connection;

    /**
     * Protected constructor because of singleton pattern
     * @param conn - Database connection object
     */
    protected Context(Database conn) {
        this.connection = conn;
    }
    public static Context getInstance() {
        if(instance == null) {
            Database db = new Memory();
            db.initialize();
            instance = new Context(db);
        }
        return instance;
    }
}
