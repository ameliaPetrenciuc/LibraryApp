import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.BookRepository;
import repository.BookRepositoryMock;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {
    private static BookRepository bookRepository;
    private static Connection connection;

    @BeforeAll
    public static void setup(){
        connection= DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        bookRepository=new BookRepositoryMock();
    }

    @Test
    public void findAll(){
        List<Book> books= bookRepository.findAll();
        assertEquals(0,books.size());
    }

    @Test
    public void findById(){
        final Optional<Book> foundBook=bookRepository.findById(1L);
        assertTrue(foundBook.isEmpty());
    }

    @Test
    public void save(){
        assertTrue(bookRepository.save(new BookBuilder().setTitle("Ion").setAuthor("Liviu Rebreanu").setPublishedDate(LocalDate.of(1900,10,2)).build()));
    }
}
