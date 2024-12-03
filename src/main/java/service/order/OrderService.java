package service.order;

import model.Order;

import java.util.List;

public interface OrderService {
    boolean save(Order order);
    List<Order> findAllOrders();
}
