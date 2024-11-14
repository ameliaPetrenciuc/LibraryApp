package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import service.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.BookDTOBuilder;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;

    public BookController(BookView bookView, BookService bookService){
        this.bookView=bookView;
        this.bookService=bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());

    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{

        public void handle(ActionEvent event){
            String title=bookView.getTitle();
            String author=bookView.getAuthor();
            Float price=bookView.getPrice();
            Long stock=bookView.getStock();

            if(title.isEmpty()|| author.isEmpty()){
                bookView.addDisplayAlertMessage("Save Error", "Problem of Author or Title field", "Can not have an empty Title or Author field");
            }else{
                BookDTO bookDTO=new BookDTOBuilder().setTitle(title).setAuthor(author).setPrice(price).setStock(stock).build();
                boolean savedBook=bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook){
                    bookView.addDisplayAlertMessage("Save Successful", "Book Added", "Book was successfully added!");
                    bookView.addBookToObservableList(bookDTO);
                }else{
                    bookView.addDisplayAlertMessage("Save Error", "Problem of adding Book", "There was a problem at adding the book to the database. Please try again!");
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            BookDTO bookDTO= (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO!=null){
                boolean deletionSuccessful=bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if(deletionSuccessful){
                    bookView.addDisplayAlertMessage("Delete Successful", "Book Deleted", "Book was successfully deleted from the database!");
                    bookView.removeBookFromObservableList(bookDTO);
                }else{
                    bookView.addDisplayAlertMessage("Delete Error", "Problem of deleting book", "There was a problem with the database. Please try again!");
                }
            }else{
                bookView.addDisplayAlertMessage("Delete Error", "Problem of deleting book", "You must select a book before pressing the delete button.");
            }
        }
    }



}
