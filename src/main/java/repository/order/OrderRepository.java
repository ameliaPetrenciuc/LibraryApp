package repository.order;

import model.Order;

import java.util.List;

public interface OrderRepository {
    boolean save(Order order);
    List<Order> findAllOrders();
//    boolean deleteOrder(Long id);
//    Optional<Order> findOrderById(Long id);
//    List<Order> findAllOrders();
//    void removeAllOrders();
}
