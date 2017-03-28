package com.authcore.router.middleware;

/**
 * Created by imreleventeracz on 26/03/17.
 */
import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;

public interface Handler {
    HandlerContext handler(HttpExchange t, HandlerContext ct) throws IOException;
}
