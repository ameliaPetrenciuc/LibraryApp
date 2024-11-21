package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import service.book.BookService;
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
        this.bookView.addSellButtonListener(new SellButtonListener());

    }
    private class SaveButtonListener implements EventHandler<ActionEvent>{

        public void handle(ActionEvent event){
            String title=bookView.getTitle();
            String author=bookView.getAuthor();
            Float price=bookView.getPrice();
            Long stock=bookView.getStock();
//            Long quantity= bookView.getQuantity();

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

    private class SellButtonListener implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event) {
            BookDTO bookDTO= (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();

            try{
                long quantity=bookView.getQuantity();

                if(quantity<=0){
                    bookView.addDisplayAlertMessage("Sell Error", "Invalid Quantity", "Quantity must be greater than 0.");
                    return;
                }

                if(bookDTO.getStock()<quantity){
                    bookView.addDisplayAlertMessage("Sell Error", "Insufficient Stock", "Not enough stock available for this sale.");
                    return;
                }

                Long newStock=bookDTO.getStock()-quantity;

                boolean updated=bookService.update(BookMapper.convertBookDTOToBook(bookDTO),newStock);

                if(updated){
                    bookDTO.setStock(newStock);
                    bookView.getBookTableView().refresh();
                    bookView.addDisplayAlertMessage("Sell Successful", "Stock Updated", "The sale was successful, and the stock has been updated.");
                }else{
                    bookView.addDisplayAlertMessage("Sell Error", "Database Error", "Could not update stock in the database.");
                }
            }catch(NumberFormatException e){
                bookView.addDisplayAlertMessage("Sell Error", "Invalid Input", "Please enter a valid quantity.");
            }
        }
    }



}
