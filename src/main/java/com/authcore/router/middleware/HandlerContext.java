package com.authcore.router.middleware;

import com.authcore.response.Response;
import com.authcore.user.User;

/**
 * Created by imreleventeracz on 28/03/17.
 * HandlerContext is for storing User data for the handlers during a request
 */
public class HandlerContext {
    public User u;
    public Response response;
    public HandlerContext(){}
}
