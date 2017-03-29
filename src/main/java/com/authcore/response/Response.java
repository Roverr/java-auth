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
 * Response class is a custom in-system implementation for response handling
 */
public class Response {
    public int status = 200;
    public Exception err;
    public HttpError httpErr;
    public JsonObject data;
    public String jwt = "";

    public Response(int status) {
        this.status = status;
    }

    public Response(HttpError e) {
        this.httpErr = e;
        this.status = e.status;
    }

    /**
     * setErrorToHttpError is a method for setting the error response based
     * on the given httpError in the Response instance
     */
    private void setErrorToHttpError() {
        this.data = Json.createObjectBuilder()
                .add("error", this.httpErr.getMessage())
                .add("success", false)
                .build();
    }

    /**
     * setBody is for setting the response body for a http response
     */
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

    /**
     * getBytes is getting the response json's byte array to
     * write to the response stream
     * @return Byte array
     * @throws UnsupportedEncodingException Should not be thrown except if
     * the running vm has no utf-8
     */
    public byte[] getBytes() throws UnsupportedEncodingException {
       return this.data.toString().getBytes("utf-8");
    }

    /**
     * Length is for getting the length of the response json body
     * @return Length of how many chars it has
     */
    public int length() {
        return this.data.toString().length();
    }

}
