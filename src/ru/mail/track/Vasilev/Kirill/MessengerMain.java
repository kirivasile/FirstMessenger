package ru.mail.track.Vasilev.Kirill;

/**
 * Created by Kirill on 29.09.2015.
 */
public class MessengerMain {
    public static void main(String[] args) {
        AuthorizationService service = new AuthorizationService(new UserStore());
        service.run();
    }
}
