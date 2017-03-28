package com.authcore.router.middleware.authenticator;

import com.authcore.response.Response;
import com.authcore.response.errors.BadRequestError;
import com.authcore.response.errors.ForbiddenError;
import com.authcore.response.errors.InternalServerError;
import com.authcore.response.errors.UnauthorizedError;
import com.authcore.router.middleware.Handler;
import com.authcore.router.middleware.HandlerContext;
import com.authcore.router.middleware.JWTParser;
import com.authcore.router.middleware.exceptions.NoTokenException;
import com.authcore.router.middleware.exceptions.UserNotFoundException;
import com.authcore.user.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Created by imreleventeracz on 28/03/17.
 */
public class Authenticator implements Handler {
    private Boolean needAuth;
    public Authenticator(Boolean needed) {
        this.needAuth = needed;
    }
    public HandlerContext handler(HttpExchange t, HandlerContext hct) throws IOException {
        try {
            User u = JWTParser.ParseJWT(t);
            hct.u = u;
            if (!this.needAuth) {
                hct.response = new Response(new ForbiddenError());
            }
        } catch (NoTokenException nte) {
            if (this.needAuth) {
                hct.response = new Response(new UnauthorizedError());
            }
        } catch (UserNotFoundException e) {
            System.out.printf("\n%s was not found in database.", e.email);
            if (this.needAuth) {
                hct.response = new Response(new BadRequestError());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            hct.response = new Response(new InternalServerError());
        } finally {
            return hct;
        }
    }
}
