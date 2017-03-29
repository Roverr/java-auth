package com.authcore;


import com.authcore.context.Context;
import com.authcore.router.Router;

public class Main {
    public static void main(String[] args) {
        // Initiate context
        Context.getInstance();
        // Create routing
        new Router();
    }
}
