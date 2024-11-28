package mapper;

import model.Book;
import model.builder.BookBuilder;
import view.model.BookDTO;
import view.model.BookDTOBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDTO convertBookToBookDTO(Book book){
        return new BookDTOBuilder()

                .setTitle(book.getTitle())
                                    .setAuthor(book.getAuthor())
                                    .setPrice(book.getPrice() != null ? book.getPrice() : 0.0f)
                                    .setStock(book.getStock() != null ? book.getStock() : 0L)
                                    .build();
    }

    public static Book convertBookDTOToBook(BookDTO bookDTO){
        return new BookBuilder().setTitle(bookDTO.getTitle())
                                .setAuthor(bookDTO.getAuthor())
                                .setPublishedDate(LocalDate.of(2010,1,1))
                                .setPrice(bookDTO.getPrice() != null ? bookDTO.getPrice() : 0.0f)
                                .setStock(bookDTO.getStock() != null ? bookDTO.getStock() : 0L)
                                .build();
    }

    public static List<Book> convertBookDTOListToBookList(List<BookDTO> bookDTOS){
        return bookDTOS.parallelStream()
                .map(BookMapper::convertBookDTOToBook)
                .collect(Collectors.toList());
    }

    public static List<BookDTO> convertBookListToBookDTOList(List<Book> books){
        return books.parallelStream()
                    .map(BookMapper::convertBookToBookDTO)
                    .collect(Collectors.toList());
    }
}
