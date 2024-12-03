package launcher;

import controller.CustomerController;
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
import view.CustomerView;
import view.EmployeeView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class CustomerComponentFactory {

    private final CustomerView customerView;
    private final CustomerController customerController;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final OrderService orderService;
    private static Boolean componentsForTest;
    private static Stage stage;
    private static Notification<User> userNotification;
    private final RightsRolesRepository rightsRolesRepository;
    private final UserRepository userRepository;
    private static CustomerComponentFactory instance;

    public static CustomerComponentFactory getInstance(Boolean aComponentsForTest, Stage primaryStage, User id, Notification<User> notification){
        if (instance == null) {
            synchronized (LoginComponentFactory.class) {
                if (instance == null) {
                    componentsForTest = aComponentsForTest;
                    stage = primaryStage;
                    userNotification = notification;
                    id=id;
                    instance = new CustomerComponentFactory(componentsForTest, stage, id, userNotification);
                }
            }
        }
        return instance;
    }

    public CustomerComponentFactory(Boolean componentsForTest, Stage primaryStage, User userId, Notification<User> user){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
        this.orderRepository=new OrderRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.orderService = new OrderServiceImpl(orderRepository);

        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(this.bookService.findAll());
        this.customerView = new CustomerView(stage, bookDTOs);
        this.customerController = new CustomerController(customerView, bookService, orderService, userId);
    }

    public CustomerView getBookView() {
        return customerView;
    }

    public CustomerController getBookController() {
        return customerController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }

    public static CustomerComponentFactory getInstance() {
        return instance;
    }
}