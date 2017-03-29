package com.authcore.config;


/**
 * Created by imreleventeracz on 27/03/17.
 * Config is a singleton class, includes all properties
 * needed for the server configuration
 */
public class Config {
    private static Config instance = null;
    public int port;
    private String secret;
    public Boolean fileStoring;
    public String fileName;
    public String logLevel;

    protected Config() {
        parseEnvironment();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /**
     * Parsing the environment variables in case of given configuration, otherwise
     * sets the config properties to its' default values
     */
    private void parseEnvironment() {
        this.port = 8000;
        this.secret = "macilaci";
        this.fileName = "./users.json";
        this.fileStoring = true;
        this.logLevel = "debug";
        String port = System.getenv("AUTH_PORT");
        if (port != null) {
            this.port = Integer.parseInt(port);
        }
        String secret = System.getenv("AUTH_SECRET");
        if (secret != null) {
            this.secret = secret;
        }
        String fileName = System.getenv("AUTH_FILE_NAME");
        if (fileName != null) {
            this.fileName = fileName;
        }
        String fileStore = System.getenv("AUTH_FILE_STORE");
        if (fileStore != null) {
            this.fileStoring = Boolean.valueOf(fileStore);
        }
        String logLevel = System.getenv("AUTH_LOG_LEVEL");
        if (logLevel != null) {
            this.logLevel = logLevel;
        }
    }

    /**
     * getSecret is a method for getting the private property
     * @return  Secret for hashing the JWT Token
     */
    public String getSecret() {
        return this.secret;
    }
}
