package com.authcore.response;

/**
 * Created by imreleventeracz on 26/03/17.
 */

import com.authcore.response.errors.InternalServerError;
import com.sun.net.httpserver.HttpExchange;
import com.authcore.response.errors.HttpError;
import javax.json.*;
import java.io.UnsupportedEncodingException;

/**
 * Created by imreleventeracz on 26/03/17.
 */
public class Response {
    public int status = 200;
    public Exception err;
    public HttpError httpErr;
    public JsonObject data;
    public String jwt = "";
    private boolean broken = false;

    public Response(int status) {
        this.status = status;
    }
    public Response(int status, String jwt) {
        this.status = status;
        this.jwt = jwt;
    }
    public Response(HttpError e) {
        this.httpErr = e;
        this.status = e.status;
    }


    public void setBroken() {
        this.broken = true;
    }

    public boolean isBroken() {
        return this.broken;
    }

    private void setErrorToHttpError() {
        this.data = Json.createObjectBuilder()
                .add("error", this.httpErr.getMessage())
                .add("success", false)
                .build();
    }

    public void setBody() {
        if (this.httpErr != null) {
            this.setErrorToHttpError();
            return;
        }
        if (this.err != null) {
            this.httpErr = new InternalServerError();
            this.setErrorToHttpError();
            return;
        }
        if (this.data != null) {
            return;
        }
        this.data = Json.createObjectBuilder()
                .add("success", true)
                .build();
    }

    public byte[] getBytes() throws UnsupportedEncodingException {
       return this.data.toString().getBytes("utf-8");
    }

    public int length() {
        return this.data.toString().length();
    }

}
