package com.authcore.response.errors;

/**
 * Created by imreleventeracz on 27/03/17.
 */
public class UnauthorizedError extends HttpError {
    public UnauthorizedError() {
        super("Unauthorized error", 401);
    }
}
