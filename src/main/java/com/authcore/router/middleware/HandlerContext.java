package com.authcore.router.middleware;

import com.authcore.response.Response;
import com.authcore.user.User;

/**
 * Created by imreleventeracz on 28/03/17.
 */
public class HandlerContext {
    public User u;
    public String token;
    public Response response;
    public HandlerContext(User u, String token) {
        this.u=u;
        this.token=token;
    }
    public HandlerContext(){}
}
