package com.authcore.core.me;

import com.authcore.response.Response;
import com.authcore.router.middleware.HandlerContext;
import com.sun.net.httpserver.HttpExchange;
import com.authcore.router.middleware.Handler;

import java.io.IOException;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Me implements Handler {
    public HandlerContext handler(HttpExchange t, HandlerContext hct) throws IOException {
        hct.response = new Response( 200);
        return hct;
    }
}
