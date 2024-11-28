package repository.order;

import model.Order;

public interface OrderRepository {
    boolean save(Order order);
//    boolean deleteOrder(Long id);
//    Optional<Order> findOrderById(Long id);
//    List<Order> findAllOrders();
//    void removeAllOrders();
}
