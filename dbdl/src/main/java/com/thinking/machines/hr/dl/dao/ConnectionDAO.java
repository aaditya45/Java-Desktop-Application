package com.thinking.machines.hr.dl.dao;
import java.sql.*;
import com.thinking.machines.hr.dl.exceptions.*;
public class ConnectionDAO{
    public static Connection getConnection() throws DAOException{
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrdb","hr","hr");
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        return connection;
    }
}
