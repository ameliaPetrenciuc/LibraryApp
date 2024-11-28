package launcher;

import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationServiceImpl;
import service.user.AuthentificationService;
import view.LoginView;

import java.sql.Connection;

//singleton lazy
public final class ComponentFactory {

    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthentificationService authentificationService;
    private final UserRepository userRepository;
//    private final OrderRespository orderRespository;
    private final RightsRolesRepository rightsRolesRepository;
//    private final BookView bookView;
//    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static ComponentFactory componentFactory;
    private static Boolean componentsForTest;
    private static Stage stage;


    private static volatile ComponentFactory instance;
//    private Object object=new Object();

   static ComponentFactory getInstance(Boolean aComponentsForTest, Stage primaryStage){
        if(instance==null){
            synchronized(ComponentFactory.class){
                if(instance==null){
                    componentsForTest = aComponentsForTest;
                    stage=primaryStage;
                    instance=new ComponentFactory(componentsForTest, primaryStage);
                }
            }
        }
        return instance;
    }

    private ComponentFactory(Boolean componentsForTest, Stage primaryStage){//am modificat din public in private
        Connection connection= DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
        this.userRepository=new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authentificationService=new AuthenticationServiceImpl(userRepository,rightsRolesRepository);
        this.loginView=new LoginView(primaryStage);
        this.loginController=new LoginController(loginView,authentificationService);
        this.bookRepository=new BookRepositoryMySQL(connection);
        this.bookService=new BookServiceImpl(bookRepository);
//        this.orderRespository=new OrderRepositoryMySQL(connection);


//        List<BookDTO> bookDTOs= BookMapper.convertBookListToBookDTOList(bookService.findAll());
//        this.bookView=new BookView(primaryStage, bookDTOs);
//        this.bookController=new BookController(bookView, bookService);
    }

    public BookService getBookService() {
        return bookService;
    }

    public static Stage getStage(){
        return stage;
    }

    public static Boolean getComponentsForTests(){
        return componentsForTest;
    }

    public AuthentificationService getAuthentificationService(){
       return authentificationService;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public LoginController getLoginController(){
       return loginController;
    }
}
