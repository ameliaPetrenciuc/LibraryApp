package repository;

import model.Book;
import model.Order;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryMySQL implements OrderRespository{
   private final Connection connection;

   public OrderRepositoryMySQL(Connection connection){
       this.connection=connection;
   }

    public boolean save(Order order) {
        //String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'" + book.getTitle()+"\', \'" + book.getPublishedDate() + "\' );";
        String newSql = "INSERT INTO orders VALUES(null,?,?);";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setLong(1, order.getStock());
            preparedStatement.setFloat(2,order.getPrice());

            preparedStatement.executeUpdate(newSql);

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public Optional<Order> findOrderById(Long id) {
        String sql="SELECT * FROM Orders WHERE id= ?";
        Optional<Order> order=Optional.empty();
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);

            ResultSet resultSet= preparedStatement.executeQuery();

            if(resultSet.next()){
                order=Optional.of(getOrderFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<Order> findAllOrders() {
        String sql="SELECT * FROM orders;";

        List<Order> orders=new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                orders.add(getOrderFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public boolean deleteOrder(Long id) {
        String newSql = "DELETE FROM orders WHERE id=?;";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeAllOrders() {
        String sql = "TRUNCATE TABLE order;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }



    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException{
        Order order=new Order();
        order.setId(resultSet.getLong("id"));
//        order.setBookId(resultSet.getLong("bookId"));
        order.setStock(resultSet.getLong("stock"));
        order.setPrice(resultSet.getFloat("price"));
        return order;
    }
}
