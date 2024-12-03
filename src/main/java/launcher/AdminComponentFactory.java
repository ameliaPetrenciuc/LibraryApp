package launcher;

import controller.AdminController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import model.User;
import model.validation.Notification;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import service.user.AuthentificationService;
import service.user.admin.AdminService;
import service.user.admin.AdminServiceImpl;
import view.AdminView;

import java.sql.Connection;

public class AdminComponentFactory {

    private final AdminView adminView;
    private final AdminController adminController;
    private final UserRepository userRepository;
    private final AdminService adminService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private static Boolean componentsForTest;
    private static Stage stage;
    private final RightsRolesRepository rightsRolesRepository;
    private static Notification<User> userNotification;


    private static AdminComponentFactory instance;

    public static AdminComponentFactory getInstance(Boolean aComponentsForTest, Stage primaryStage, Notification<User> notification) {
        if(instance==null){
            synchronized(LoginComponentFactory.class){
                if(instance==null){
                    componentsForTest = aComponentsForTest;
                    stage=primaryStage;
                    userNotification=notification;
                    instance=new AdminComponentFactory(componentsForTest, stage, userNotification);
                }
            }
        }
        return instance;
    }

    private AdminComponentFactory(Boolean componentsForTest, Stage primaryStage, Notification<User> notification) {
        Connection connection= DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
      //  RightsRolesRepository rightsRolesRepository = getRightsRolesRepository();
        this.rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.adminService = new AdminServiceImpl(userRepository,rightsRolesRepository);
        this.orderRepository=new OrderRepositoryMySQL(connection);
        this.orderService = new OrderServiceImpl(orderRepository);

        this.adminView = new AdminView(stage);
        this.adminController = new AdminController(adminView, adminService, userNotification, orderService);
    }

    public static Boolean getComponentsForTest() {
        return componentsForTest;
    }

    public static Stage getStage() {
        return stage;
    }

    public static Notification<User> getUserNotification() {
        return userNotification;
    }
}
