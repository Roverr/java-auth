package com.authcore.router.middleware;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.authcore.config.Config;
import com.authcore.context.Context;
import com.authcore.router.middleware.exceptions.NoTokenException;
import com.authcore.router.middleware.exceptions.UserNotFoundException;
import com.authcore.user.User;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;


/**
 * Created by imreleventeracz on 28/03/17.
 */
public final class JWTParser {
    private JWTParser(){};
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
        User u = server.connection.GetUserByEmail(email);
        if (u == null) {
            throw new UserNotFoundException(email);
        }
        return u;
    }
}
