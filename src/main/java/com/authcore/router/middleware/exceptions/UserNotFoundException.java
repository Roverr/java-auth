package com.authcore.router.middleware.exceptions;

/**
 * Created by imreleventeracz on 28/03/17.
 */
public class UserNotFoundException extends Exception {
    public String email;
    public UserNotFoundException(String email){ this.email=email; }
}
