import controller.LoginController;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Book;
import model.builder.BookBuilder;
import model.validation.UserValidator;
import repository.BookRepository;
//import repository.BookRepositoryMock;
import repository.BookRepositoryCacheDecorator;
import repository.BookRepositoryMySQL;
import repository.Cache;
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
import java.time.LocalDate;

import static database.Constants.Schemas.PRODUCTION;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
//        System.out.println("Hello world!");
//
//        Book book=new BookBuilder()
//                .setTitle("Ion")
//                .setAuthor("Liviu Rebreanu")
//                .setPublishedDate(LocalDate.of(1910,10,20))
//                .build();
//
//        System.out.println(book);
//
//        BookRepository bookRepository=new BookRepositoryMock();
//        bookRepository.save(book);
//        bookRepository.save(new BookBuilder().setAuthor("Ioan Slavici").setTitle("Moara cu noroc").setPublishedDate(LocalDate.of(1950,2,10)).build());
//        System.out.println(bookRepository.findAll());
//        bookRepository.removeAll();
//        System.out.println(bookRepository.findAll());

//        Connection connection=DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
//        BookRepository bookRepository=new BookRepositoryCacheDecorator(new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()),new Cache<>());
//        BookService bookService=new BookServiceImpl(bookRepository);
//
//        RightsRolesRepository rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
//        UserRepository userRepository=new UserRepositoryMySQL(connection,rightsRolesRepository);
//        AuthentificationService authentificationService=new AuthenticationServiceImpl(userRepository,rightsRolesRepository);
//        if(userRepository.existsByUsername("Amelia")){
//            System.out.println("Username already present into the user table!");
//        }else{
//            authentificationService.register("Amelia","parola123!");
//        }
        //System.out.println(authentificationService.login("Amelia","parola123!"));
//       bookService.save(book);
//       System.out.println(bookService.findAll());
//        System.out.println(bookService.findAll());
//        Book bookMoaraCuNoroc=new BookBuilder().setAuthor("Ioan Slavici").setTitle("Moara cu noroc").setPublishedDate(LocalDate.of(1950,2,10)).build();
//        bookRepository.save(bookMoaraCuNoroc);
//        System.out.println(bookRepository.findAll());
//        bookService.delete(bookMoaraCuNoroc);
//        bookService.delete(book);
//        System.out.println(bookRepository.findAll());


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Connection connection=new JDBConnectionWrapper(PRODUCTION).getConnection();

        final RightsRolesRepository rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository=new UserRepositoryMySQL(connection,rightsRolesRepository);

        final AuthentificationService authentificationService=new AuthenticationServiceImpl(userRepository,rightsRolesRepository);

        final LoginView loginView=new LoginView(primaryStage);
        final UserValidator userValidator=new UserValidator(userRepository);
        new LoginController(loginView, authentificationService,userValidator);
    }
}