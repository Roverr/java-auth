package com.authcore.response.errors;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class NotFoundError extends HttpError {
    public NotFoundError() {
        super( "Resource not found", 404);
    }
}
