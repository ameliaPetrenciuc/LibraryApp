package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Order;
import model.User;
import service.book.BookService;
import service.order.OrderService;
import view.CustomerView;
import view.model.BookDTO;

import java.time.LocalDateTime;

public class CustomerController {
    private final CustomerView customerView;
    private final BookService bookService;
    private final OrderService orderService;
    private User loggedInUser;

    public CustomerController(CustomerView customerView, BookService bookService,OrderService orderService, User loggedInUser){
        this.customerView=customerView;
        this.bookService=bookService;
        this.orderService=orderService;
        this.loggedInUser=loggedInUser;

        this.customerView.addBuyButtonListener(new SellButtonListener());

    }

    private class SellButtonListener implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event) {
            BookDTO bookDTO= (BookDTO) customerView.getBookTableView().getSelectionModel().getSelectedItem();

            try{
                long quantity=customerView.getQuantity();

                if(quantity<=0){
                    customerView.addDisplayAlertMessage("Sell Error", "Invalid Quantity", "Quantity must be greater than 0.");
                    return;
                }

                if(bookDTO.getStock()<quantity){
                    customerView.addDisplayAlertMessage("Sell Error", "Insufficient Stock", "Not enough stock available for this sale.");
                    return;
                }

                Long newStock=bookDTO.getStock()-quantity;

                boolean updated=bookService.update(BookMapper.convertBookDTOToBook(bookDTO),newStock);

                if(updated){
                    bookDTO.setStock(newStock);
                    customerView.getBookTableView().refresh();


                    Order order = new Order();

                    order.setEmployeeId(loggedInUser.getId());
                    order.setTitle(bookDTO.getTitle());
                    order.setAuthor(bookDTO.getAuthor());
                    order.setQuantity((int) quantity);
                    order.setSaleDateTime(LocalDateTime.now()); // Momentul actual

                    boolean orderSaved=orderService.save(order);

                    if (orderSaved) {
                        customerView.addDisplayAlertMessage("Sell Successful", "Order Created", "The sale was successful, and the order has been saved.");
                    } else {
                        customerView.addDisplayAlertMessage("Sell Error", "Order Error", "The sale was successful, but the order could not be saved.");
                    }
                } else {
                    customerView.addDisplayAlertMessage("Sell Error", "Database Error", "Could not update stock in the database.");
                }
            } catch (NumberFormatException e) {
                customerView.addDisplayAlertMessage("Sell Error", "Invalid Input", "Please enter a valid quantity.");
            }
        }
    }



}
