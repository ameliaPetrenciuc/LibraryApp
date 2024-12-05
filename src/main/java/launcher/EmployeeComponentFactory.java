package launcher;

import controller.EmployeeController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.User;
import model.validation.Notification;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import view.EmployeeView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class EmployeeComponentFactory {

    private final EmployeeView employeeView;
    private final EmployeeController employeeController;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final OrderService orderService;
    private static volatile EmployeeComponentFactory instance;
    private static Boolean componentsForTest;
    private static Stage stage;
    private static Notification<User> userNotification;
    private static User id;
    private final RightsRolesRepository rightsRolesRepository;
    private final UserRepository userRepository;

    public static EmployeeComponentFactory getInstance(Boolean aComponentsForTest, Stage primaryStage, User id, Notification<User> notification){
        if (instance == null) {
            synchronized (LoginComponentFactory.class) {
                if (instance == null) {
                    componentsForTest = aComponentsForTest;
                    stage = primaryStage;
                    userNotification = notification;
                    id=id;
                    instance = new EmployeeComponentFactory(componentsForTest, stage, id, userNotification);
                }
            }
        }
        return instance;
    }

    private EmployeeComponentFactory(Boolean componentsForTest, Stage primaryStage, User userId, Notification<User> user){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
        this.orderRepository=new OrderRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.orderService = new OrderServiceImpl(orderRepository);

        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(this.bookService.findAll());
        this.employeeView = new EmployeeView(stage, bookDTOs);
        this.employeeController = new EmployeeController(employeeView, bookService, orderService, userId);
    }
}