package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {

    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection=connection;
    }
    @Override
    public List<Book> findAll() {
        String sql="SELECT * FROM book;";

        List<Book> books=new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql="SELECT * FROM book WHERE id= ?";
        Optional<Book> book=Optional.empty();
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            ResultSet resultSet= preparedStatement.executeQuery();

            if(resultSet.next()){
                book=Optional.of(getBookFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return book;
    }

    //de modificat
    @Override
    public boolean save(Book book) {
        //String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'" + book.getTitle()+"\', \'" + book.getPublishedDate() + "\' );";
        String newSql = "INSERT INTO book VALUES(null,?,?,?,?,?);";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setDate(3, Date.valueOf(book.getPublishedDate()));
//            Long stock = book.getStock() != null ? book.getStock() : 0L;
            preparedStatement.setFloat(4, book.getPrice());
//            Float price = book.getPrice() != null ? book.getPrice() : 0.0f;
            preparedStatement.setLong(5, book.getStock());


//            preparedStatement.setFloat(5, book.getPrice());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Book book) {
        String newSql = "DELETE FROM book WHERE author=\'" + book.getAuthor() +"\' AND title=\'" + book.getTitle()+"\';";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(newSql);

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    @Override
//    public boolean update(Book book, Long stock) {
//        return false;
//    }

    @Override
    public void removeAll() {
        String sql = "TRUNCATE TABLE book;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean update(Book book, Long stock){
        String sql = "UPDATE book SET stock = ? WHERE author = ? and title=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, stock);
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getTitle());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        Date publishedDate = resultSet.getDate("publishedDate");
        LocalDate localPublishedDate = (publishedDate != null) ? publishedDate.toLocalDate() : null;

        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(localPublishedDate)
                .setPrice(resultSet.getFloat("price"))
                .setStock(resultSet.getLong("stock"))
                .build();
    }
}
