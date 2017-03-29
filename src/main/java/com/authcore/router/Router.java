package com.authcore.router;

import com.authcore.config.Config;
import com.authcore.core.login.Login;
import com.authcore.core.me.Me;
import com.authcore.core.registration.Registration;
import com.authcore.logger.Logger;
import com.authcore.router.middleware.Middleware;
import com.authcore.router.middleware.authenticator.Authenticator;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/**
 * Created by imreleventeracz on 27/03/17.
 * Router is an implementation of a http server router
 * initialize routes and creates environment for them
 */
public class Router {
    public HttpServer server;
    private Authenticator noAuth = new Authenticator(false);
    private Authenticator auth = new Authenticator(true);

    /**
     * Constructor which creates the server
     */
    public Router() {
        Config config = Config.getInstance();
        Middleware login = login();
        Middleware registration = registration();
        Middleware me = me();
        try {
            InetSocketAddress port = new InetSocketAddress(config.port);
            this.server = HttpServer.create(port, 0);
            this.server.createContext("/login", login);
            this.server.createContext("/registration", registration);
            this.server.createContext("/me", me);
            this.server.setExecutor(null); // creates a default executor
            this.server.start();
            Logger.Println(String.format("Server is listening on %d", config.port));
        } catch (Exception e) {
            Logger.Errorln(e);
        }
    }

    /**
     * login is a middleware for the login endpoint
     * @return Middleware handler for login
     */
    private Middleware login() {
        Middleware login = new Middleware();
        Login handler = new Login();
        login.add(this.noAuth).add(handler);
        return login;
    }

    /**
     * registration is a middleware for the registration endpoint
     * @return Middleware handler for registration
     */
    private Middleware registration() {
        Middleware registration = new Middleware();
        Registration handler = new Registration();
        registration.add(this.noAuth).add(handler);
        return registration;
    }

    /**
     * me is a middleware for the me endpoint
     * @return Middleware handler for me
     */
    private Middleware me() {
        Middleware me = new Middleware();
        Me handler = new Me();
        return me.add(this.auth).add(handler);
    }
}
