package repository.user;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
//import model.validator.Notification;
import model.validation.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//import static database.Bootstrap.rightsRolesRepository;
import static database.Constants.Tables.USER;
import static java.util.Collections.singletonList;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {

        String sql="SELECT * FROM user;";

        List<User> users=new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                users.add(getUserFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

       Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        String fetchUserSql =
                "Select * from USER WHERE username=? AND password=?";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet userResultSet = preparedStatement.executeQuery();
            if(userResultSet.next()){
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
            }else{
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }
        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {
        Notification<Boolean> saveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);//returneaza id
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());

            if(existsByUsername(user.getUsername())){
                saveNotification.addError("Username already exists!");
                saveNotification.setResult(Boolean.FALSE);
            }else{
                insertUserStatement.executeUpdate();
                ResultSet rs = insertUserStatement.getGeneratedKeys();
                rs.next();
                long userId = rs.getLong(1);
                user.setId(userId);
                rightsRolesRepository.addRolesToUser(user,user.getRoles());
                saveNotification.setResult(Boolean.TRUE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            saveNotification.addError("Database Error");
        }
        return saveNotification;
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + email + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {

        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                .build();
    }

}