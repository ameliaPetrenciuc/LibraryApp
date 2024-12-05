package service.user;

import model.User;
import model.validation.Notification;

public interface AuthentificationService {
    Notification<Boolean> register(String username, String password);
    Notification<Boolean> register(String username, String password, String role);
    Notification<User> login(String username, String password);
    Long getLoggedInCustomerId();
    boolean logout(User user);
}
