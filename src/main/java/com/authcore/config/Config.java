package com.authcore.config;


/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Config {
    private static com.authcore.config.Config instance = null;
    public int port;
    private String secret;
    protected Config() {
        parseEnvironment();
    }
    private void parseEnvironment() {
        this.port = 8000;
        this.secret = "macilaci";
        String port = System.getenv("AUTH_PORT");
        if (port != null) {
            this.port = Integer.parseInt(port);
        }
        String secret = System.getenv("AUTH_SECRET");
        if (secret != null) {
            this.secret = secret;
        }
    }
    public String getSecret() {
        return this.secret;
    }
    public static com.authcore.config.Config getInstance() {
        if(instance == null) {
            instance = new com.authcore.config.Config();
        }
        return instance;
    }
}
