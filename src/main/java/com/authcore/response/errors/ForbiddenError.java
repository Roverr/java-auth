package com.authcore.response.errors;

/**
 * Created by imreleventeracz on 28/03/17.
 */
public class ForbiddenError extends HttpError {
    public ForbiddenError() {
        super("Forbidden error", 403);
    }
}
