package com.github.kirivasile.technotrack;

/**
 * Created by Kirill on 29.09.2015.
 */
public class MessengerMain {
    public static void main(String[] args) {
        UserStore userStore = new UserStore();
        AuthorizationService service = new AuthorizationService(userStore);
        service.run();
    }
}
