package repository.order;

import model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryMySQL implements OrderRepository {
   private final Connection connection;

   public OrderRepositoryMySQL(Connection connection){
       this.connection=connection;
   }

    public boolean save(Order order) {
        String newSql = "INSERT INTO orders VALUES(null,?,?,?,?,?);";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setLong(1, order.getEmployeeId());
            preparedStatement.setString(2,order.getTitle());
            preparedStatement.setString(3, order.getAuthor());
            preparedStatement.setInt(4, order.getQuantity());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(order.getSaleDateTime())); // Conversie LocalDateTime în Timestamp

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Order> findAllOrders(){
        String sql = "SELECT * FROM orders;";

        List<Order> orders = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                orders.add(getOrderFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException{

        Order order=new Order();
        order.setId(resultSet.getLong("id"));
        order.setEmployeeId(resultSet.getLong("user_id"));
        order.setAuthor(resultSet.getString("author"));
        order.setTitle(resultSet.getString("title"));
        order.setQuantity(resultSet.getInt("quantity"));
        order.setSaleDateTime(resultSet.getTimestamp("sale_date").toLocalDateTime());
       return order;
    }
}
