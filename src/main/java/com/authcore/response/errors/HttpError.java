package com.authcore.response.errors;

/**
 * Created by imreleventeracz on 26/03/17.
 */
public class HttpError extends Exception {
    public int status;
    public HttpError(String msg, int status) {
        super(msg);
        this.status = status;
    }
}