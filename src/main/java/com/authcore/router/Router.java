package com.authcore.router;

import com.authcore.config.Config;
import com.authcore.core.login.Login;
import com.authcore.core.me.Me;
import com.authcore.core.registration.Registration;
import com.authcore.router.middleware.Middleware;
import com.authcore.router.middleware.authenticator.Authenticator;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class Router {
    public HttpServer server;
    private Authenticator noAuth = new Authenticator(false);
    private Authenticator auth = new Authenticator(true);
    public Router() {
        Config config = Config.getInstance();
        Middleware login = Login();
        Middleware registration = Registration();
        Middleware me = Me();
        try {
            InetSocketAddress port = new InetSocketAddress(config.port);
            this.server = HttpServer.create(port, 0);
            this.server.createContext("/login", login);
            this.server.createContext("/registration", registration);
            this.server.createContext("/me", me);
            this.server.setExecutor(null); // creates a default executor
            this.server.start();
            System.out.printf("Server is listening on %d", 8000);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public Middleware Login() {
        Middleware login = new Middleware();
        Login handler = new Login();
        login.Add(this.noAuth).Add(handler);
        return login;
    }

    public Middleware Registration() {
        Middleware registration = new Middleware();
        Registration handler = new Registration();
        registration.Add(this.noAuth).Add(handler);
        return registration;
    }

    public Middleware Me() {
        Middleware me = new Middleware();
        Me handler = new Me();
        return me.Add(this.auth).Add(handler);
    }
}
