package com.authcore.core.login;

import java.io.IOException;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.authcore.config.Config;
import com.authcore.context.Context;
import com.authcore.response.errors.BadRequestError;
import com.authcore.response.errors.InternalServerError;
import com.authcore.response.errors.NotFoundError;
import com.authcore.response.errors.UnauthorizedError;
import com.authcore.router.middleware.HandlerContext;
import com.sun.net.httpserver.*;
import com.authcore.response.Response;
import com.authcore.router.middleware.Handler;
import javax.json.*;
import com.authcore.core.Core;
import com.authcore.user.User;

/**
 * Created by imreleventeracz on 26/03/17.
 */
public class Login extends Core implements Handler{

    protected Boolean isBodyValid(JsonObject body) {
        Boolean isEmailMissing = body.getString("email", "").equalsIgnoreCase("");
        Boolean isPasswordMissing = body.getString("password", "").equalsIgnoreCase("");
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
        Context server = Context.getInstance();
        Config config = Config.getInstance();
        User u = server.connection.GetUserByEmail(body.getString("email"));
        if (u == null) {
            hct.response = new Response(new NotFoundError());
            return hct;
        }
        Boolean isValidUser = u.validatePassword(body.getString("password"));
        if (!isValidUser) {
            hct.response = new Response(new UnauthorizedError());
            return hct;
        }
        Response success = new Response(200);
        try {
            Algorithm algorithm = Algorithm.HMAC512(config.getSecret());
            Date today = new Date();
            Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
            String token = JWT.create()
                    .withIssuer("Rover-Auth")
                    .withClaim("email", u.email)
                    .withClaim("name", u.name)
                    .withExpiresAt(tomorrow)
                    .sign(algorithm);
            success.jwt = token;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            hct.response = new Response(new InternalServerError());
            return hct;
        }
        hct.response = success;
        return hct;
    }
}