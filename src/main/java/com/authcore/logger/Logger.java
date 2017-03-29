package com.authcore.logger;

import com.authcore.config.Config;

/**
 * Created by imreleventeracz on 29/03/17.
 * Logger class is a minimal implementation for logging
 */
public class Logger {

    /**
     * Private constructor because of static class
     */
    private Logger() {
    }

    /**
     * Debugln is for debug messages
     * @param s Message to write out for debug
     */
    public static void Debugln(String s) {
        Config config = Config.getInstance();
        if (config.logLevel.equalsIgnoreCase("DEBUG")) {
            System.out.printf("\nDEBUG || %s\n", s);
        }
    }

    /**
     * Println is for info messages
     * @param s Message to write out for info
     */
    public static void Println(String s) {
        System.out.printf("\nINFO || %s\n", s);
    }

    /**
     * Errorln is for error messages
     * @param e Exception to write out
     */
    public static void Errorln(Exception e) {
        System.err.printf("\nERROR || %s", e.getMessage());
        e.printStackTrace();
    }
}
