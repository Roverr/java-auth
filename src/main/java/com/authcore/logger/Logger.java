package com.authcore.logger;

import com.authcore.config.Config;

/**
 * Created by imreleventeracz on 29/03/17.
 */
public class Logger {
    private Logger() {
    }
    public static void Debugln(String s) {
        Config config = Config.getInstance();
        if (config.logLevel.equalsIgnoreCase("DEBUG")) {
            System.out.printf("\nDEBUG || %s\n", s);
        }
    }
    public static void Println(String s) {
        System.out.printf("\nINFO || %s\n", s);
    }
    public static void Errorln(Exception e) {
        System.err.printf("\nERROR || %s", e.getMessage());
        e.printStackTrace();
    }
}
