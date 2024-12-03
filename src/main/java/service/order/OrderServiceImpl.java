package service.order;

import model.Book;
import model.Order;
import repository.book.BookRepository;
import repository.order.OrderRepository;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl (OrderRepository orderRepository){
        this.orderRepository=orderRepository;
    }
    @Override
    public boolean save(Order order) {
        return orderRepository.save(order);
    }
    public List<Order> findAllOrders() {
        return orderRepository.findAllOrders();
    }
}
