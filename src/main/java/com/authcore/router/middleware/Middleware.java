package com.authcore.router.middleware;

import java.io.OutputStream;
import java.util.ArrayList;

import com.authcore.logger.Logger;
import com.authcore.user.User;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.util.Iterator;
import com.authcore.response.Response;
import com.authcore.response.errors.InternalServerError;

/**
 * Created by imreleventeracz on 26/03/17.
 * Middleware is an implementation of middleware pattern commonly used in web servers
 */
public class Middleware implements HttpHandler {
    // handlers store the chained handlers in the middleware
    private final ArrayList<Handler> handlers;

    public Middleware() {
        this.handlers = new ArrayList();
    }

    /**
     * Add is for adding a handler function to the chain
     * @param h Handler to add
     * @return this
     */
    public Middleware Add(Handler h) {
        this.handlers.add(h);
        return this;
    }

    /**
     * handleChainBreak is for deciding if a handler has broken the chain
     * @param r Response which is received from the handler
     * @return True if response has an error
     */
    private Boolean handleChainBreak (Response r) {
        if (r == null) {
            return false;
        }
        if (r.httpErr != null) {
            return true;
        }
        if (r.err != null) {
            r.httpErr = new InternalServerError();
            return true;
        }
        return false;
    }

    /**
     * Chain is a method for chaning the handlers after each other
     * when a http request falls in
     * @param t HttpExchange data
     * @return Final response
     */
    private Response chain(HttpExchange t) {
        Iterator<Handler> handlerIterator = this.handlers.iterator();
        Handler next;
        HandlerContext hct = new HandlerContext();
        while (handlerIterator.hasNext()) {
            next = handlerIterator.next();
            try {
                hct = next.handler(t, hct);
                Boolean broken = handleChainBreak(hct.response);
                if (broken) {
                    return hct.response;
                }
            } catch (Exception e) {
                Logger.Errorln(e);
                return new Response(new InternalServerError());
            }
        }
        return hct.response;
    }

    /**
     * Handle is an implementation which can be used with
     * HttpServer context. It chains the handlers and write the response
     * body
     * @param t HttpExchange http data
     * @throws IOException Http body writing reading can cause exception
     */
    public void handle(HttpExchange t) throws IOException {
        Response response = this.chain(t);
        if (response.err != null) {
            Logger.Errorln(response.err);
        }
        OutputStream os = t.getResponseBody();
        t.getResponseHeaders().add("Content-Type", "application/json");
        if (response.jwt != null) {
            t.getResponseHeaders().add("Authentication", "Bearer " + response.jwt);
        }
        response.setBody();
        byte[] body = null;
        t.sendResponseHeaders(response.status, response.length());
        try {
            body = response.getBytes();
        } catch(Exception e) {
            Logger.Errorln(e);
        }
        if (body != null) {
            os.write(body);
        }
        os.close();
        return;
    }
}

