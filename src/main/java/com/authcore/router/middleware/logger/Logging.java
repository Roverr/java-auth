package com.authcore.router.middleware.logger;

import com.authcore.logger.Logger;
import com.authcore.router.middleware.Handler;
import com.authcore.router.middleware.HandlerContext;
import com.sun.javafx.binding.StringFormatter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Created by imreleventeracz on 07/04/17.
 * Logging is a middleware for a minimal info about the requests
 */
public class Logging implements Handler {
    // Path of the chain
    private String path;
    public Logging(String endpoint) {
        this.path = endpoint;
    }
    public HandlerContext handler(HttpExchange t, HandlerContext ct) throws IOException {
        String text = StringFormatter.format("%s  %s  %d",
                t.getRequestMethod(),
                this.path,
                ct.response.status).getValue();
        Logger.Println(text);
        return ct;
    }
}
