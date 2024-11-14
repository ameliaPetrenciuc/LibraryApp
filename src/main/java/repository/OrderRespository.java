package repository;

import model.Book;
import model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRespository {
    boolean save(Order order);
    boolean deleteOrder(Long id);
    Optional<Order> findOrderById(Long id);
    List<Order> findAllOrders();
    void removeAllOrders();
}
