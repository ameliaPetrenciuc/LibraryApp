package service.user.admin;

import model.Order;
import model.User;
import model.validation.Notification;

import java.util.List;
import java.util.Map;

public interface AdminService {
    //Notification<Boolean> addUser(String username, String password, String role);
    boolean deleteUserById(Long id);
    List<User> findAll();
    boolean existsByUsername(String username);
    Notification<User> findUser(Long id);
    void removeAll();
    boolean generateOrderReport(List<Order> orders);
    Map<Long, String> getEmployeeIdToUsernameMap();
    Notification<Boolean> register(String username, String password);
    Notification<Boolean> register(String username, String password, String role);
}
