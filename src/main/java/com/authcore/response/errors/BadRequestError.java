package com.authcore.response.errors;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class BadRequestError extends HttpError {
    public BadRequestError() {
        super("Bad request", 400);
    }
}
