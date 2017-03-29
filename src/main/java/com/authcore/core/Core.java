package com.authcore.core;

import com.authcore.logger.Logger;
import com.sun.net.httpserver.HttpExchange;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by imreleventeracz on 27/03/17.
 * Core is an abstract class for http handlers
 */
abstract public class Core {
    /**
     * isBodyValid is a method for checking if the request body has
     * every property needed for the handler
     * @param body JsonObject with the body
     * @return Returns if body is valid or not
     */
    protected abstract Boolean isBodyValid(JsonObject body);

    /**
     * parseBody is a method for parsing the request body from a http
     * request into JsonObject
     * @param t HttpExchange with the byte encoded body
     * @return Returns the parsed JsonObject
     * @throws IOException Error can happen if the reading from
     * he request body goes wrong
     */
    protected JsonObject parseBody(HttpExchange t) throws IOException {
        InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        JsonReader jsonReader = null;
        try {
            jsonReader = Json.createReader(br);
        } catch (Exception e) {
            Logger.Errorln(e);
        }
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        br.close();
        isr.close();
        return object;
    }
}
