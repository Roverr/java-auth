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
 */
public class Middleware implements HttpHandler {
    private final ArrayList<Handler> handlers;

    public Middleware() {
        this.handlers = new ArrayList();
    }

    public Middleware Add(Handler h) {
        this.handlers.add(h);
        return this;
    }

    private Boolean handleChainBreak (Response r) {
        if (r == null) {
            return false;
        }
        if (r.httpErr != null) {
            r.setBroken();
            return true;
        }
        if (r.err != null) {
            r.httpErr = new InternalServerError();
            r.setBroken();
            return true;
        }
        return false;
    }

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

