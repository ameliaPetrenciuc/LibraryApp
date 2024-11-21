package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnectionWrapper {
    private static final String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
    private static final String DB_URL="jdbc:mysql://localhost/";
    private static final String USER="root";
    private static final String PASSWORD="AME_amelia14";
    private static final int TIMEOUT=5;
    private Connection connection;

    public JDBConnectionWrapper(String schema){
        try{
            Class.forName(JDBC_DRIVER);
            connection= DriverManager.getConnection(DB_URL+schema, USER,PASSWORD);
            createTables();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement statement= connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS book(" +
                " id BIGINT NOT NULL AUTO_INCREMENT, " +
                " title VARCHAR(500) NOT NULL, " +
                " author VARCHAR(500) NOT NULL, " +
                " publishedDate DATETIME DEFAULT NULL, " +
                " price FLOAT DEFAULT 0, " +
                " stock BIGINT DEFAULT 0, " +
                " PRIMARY KEY(id), " +
                " UNIQUE KEY id_UNIQUE(id) " +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

        String orderTableSql = "CREATE TABLE IF NOT EXISTS orders(" +
                " id BIGINT NOT NULL AUTO_INCREMENT, " +
                " stock BIGINT NOT NULL, " +
                " price FLOAT NOT NULL, " +
                " PRIMARY KEY(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(orderTableSql);

    }

    public boolean testConnection() throws SQLException{
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection(){
        return connection;
    }



}
