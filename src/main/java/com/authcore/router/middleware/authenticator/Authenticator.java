package com.authcore.router.middleware.authenticator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.authcore.config.Config;
import com.authcore.context.Context;
import com.authcore.logger.Logger;
import com.authcore.response.Response;
import com.authcore.response.errors.BadRequestError;
import com.authcore.response.errors.ForbiddenError;
import com.authcore.response.errors.InternalServerError;
import com.authcore.response.errors.UnauthorizedError;
import com.authcore.router.middleware.Handler;
import com.authcore.router.middleware.HandlerContext;
import com.authcore.router.middleware.exceptions.NoTokenException;
import com.authcore.router.middleware.exceptions.UserNotFoundException;
import com.authcore.user.User;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

/**
 * Created by imreleventeracz on 28/03/17.
 * Authenticator is a middleware function for parsing JWT token
 */
public class Authenticator implements Handler {
    private Boolean needAuth;

    /**
     * Constructor for authenticator middleware
     * @param needed Boolean to indicate if authentication is needed or rejected if have any
     */
    public Authenticator(Boolean needed) {
        this.needAuth = needed;
    }
    public HandlerContext handler(HttpExchange t, HandlerContext hct) throws IOException {
        try {
            User u = this.ParseJWT(t);
            hct.u = u;
            if (!this.needAuth) {
                hct.response = new Response(new ForbiddenError());
            }
        } catch (NoTokenException nte) {
            if (this.needAuth) {
                hct.response = new Response(new UnauthorizedError());
            }
        } catch (UserNotFoundException e) {
            Logger.Debugln(String.format("%s was not found in database", e.email));
            hct.response = new Response(new BadRequestError());
        } catch (Exception e) {
            Logger.Errorln(e);
            hct.response = new Response(new InternalServerError());
        } finally {
            return hct;
        }
    }

    public static User ParseJWT(HttpExchange t) throws Exception {
        Headers gg = t.getRequestHeaders();
        String lg = "Authentication";
        List<String> bearerList = gg.get(lg);
        if (bearerList == null) {
            throw new NoTokenException();
        }
        String bearer = bearerList.get(0);
        if (bearer == null) {
            throw new NoTokenException();
        }
        String token = bearer.replace("Bearer ", "");
        Config config = Config.getInstance();
        Algorithm algorithm = Algorithm.HMAC512(config.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Rover-Auth")
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        String email = jwt.getClaim("email").asString();
        Context server = Context.getInstance();
        User u = server.connection.getUserByEmail(email);
        if (u == null) {
            throw new UserNotFoundException(email);
        }
        return u;
    }
}
