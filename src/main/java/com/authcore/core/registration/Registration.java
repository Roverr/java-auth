package com.authcore.core.registration;

import com.authcore.core.Core;
import com.authcore.response.errors.BadRequestError;
import com.authcore.response.errors.NotFoundError;
import com.authcore.router.middleware.Handler;
import com.authcore.response.Response;
import com.authcore.router.middleware.HandlerContext;
import com.authcore.user.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import javax.json.*;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Registration extends Core implements Handler {

    protected Boolean isBodyValid(JsonObject body) {
        Boolean isEmailMissing = body.getString("email").equalsIgnoreCase("");
        Boolean isPasswordMissing = body.getString("password").equalsIgnoreCase("");
        if (isEmailMissing || isPasswordMissing) {
            return false;
        }
        return true;
    }

    public HandlerContext handler(HttpExchange t, HandlerContext hct) throws IOException {
        Boolean POST = t.getRequestMethod().equalsIgnoreCase("POST");
        if (!POST) {
            hct.response = new Response(new NotFoundError());
            return hct;
        }
        JsonObject body = this.parseBody(t);
        Boolean valid = isBodyValid(body);
        if (!valid) {
            hct.response = new Response(new BadRequestError());
            return hct;
        }
        User.Create(body.getString("email"), body.getString("name", ""), body.getString("password"));
        hct.response = new Response(200);
        return hct;
    }

}
