package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.Book;
import repository.BookRepository;
import repository.BookRepositoryMySQL;
import service.BookService;
import service.BookServiceImpl;
import view.BookView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

//singleton lazy
public final class ComponentFactory {

    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;

    private final BookService bookService;
    private static volatile ComponentFactory instance;
//    private Object object=new Object();

   static ComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage){
        if(instance==null){
            synchronized(ComponentFactory.class){
                if(instance==null){
                    instance=new ComponentFactory(componentsForTest, primaryStage);
                }
            }
        }
        return instance;
    }

    private ComponentFactory(Boolean componentsForTest, Stage primaryStage){//am modificat din public in private
        Connection connection= DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository=new BookRepositoryMySQL(connection);
        this.bookService=new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs= BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.bookView=new BookView(primaryStage, bookDTOs);
        this.bookController=new BookController(bookView, bookService);
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}
