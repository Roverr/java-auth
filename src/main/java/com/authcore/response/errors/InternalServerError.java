package com.authcore.response.errors;

/**
 * Created by imreleventeracz on 26/03/17.
 */
public class InternalServerError extends HttpError {
    public InternalServerError() {
        super( "Internal server error", 500);
    }
}
