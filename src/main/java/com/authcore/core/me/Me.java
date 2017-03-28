package com.authcore.core.me;

import com.authcore.response.Response;
import com.authcore.response.errors.NotFoundError;
import com.authcore.router.middleware.HandlerContext;
import com.authcore.user.User;
import com.sun.net.httpserver.HttpExchange;
import com.authcore.router.middleware.Handler;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Me implements Handler {
    public HandlerContext handler(HttpExchange t, HandlerContext hct) throws IOException {
        Boolean GET = t.getRequestMethod().equalsIgnoreCase("GET");
        if (!GET) {
            hct.response = new Response(new NotFoundError());
            return hct;
        }
        User u =  hct.u;
        JsonObject user = Json.createObjectBuilder()
                .add("email", u.email)
                .add("name", u.name)
                .build();
        JsonObject data = Json.createObjectBuilder()
                .add("data", user)
                .build();
        hct.response = new Response( 200);
        hct.response.data = data;
        return hct;
    }
}
