package com.authcore.core;

import com.sun.net.httpserver.HttpExchange;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by imreleventeracz on 27/03/17.
 */
abstract public class Core {
    protected JsonObject parseBody(HttpExchange t) throws IOException {
        InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        JsonReader jsonReader = null;
        try {
             jsonReader = Json.createReader(br);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        br.close();
        isr.close();
        return object;
    }
}
