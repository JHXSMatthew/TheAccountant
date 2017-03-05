package com.github.JHXSMatthew.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Matthew on 28/02/2017.
 */
public class MySQLConnection {
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/web";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String MAX_POOL = "250"; // set your own limit

    private Connection connection;
    private Properties properties;

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    public Connection connect(){
        if(connection == null){
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL,getProperties());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void disconnect(){
        if(connection != null){
            try{
                connection.close();
                connection = null;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }



}
