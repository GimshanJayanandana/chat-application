package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.RegistrationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationModel {
    public boolean isValidUser(String userName , String password) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE user_name = ? AND password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,userName);
        ptsm.setString(2,password);

        ResultSet resultSet = ptsm.executeQuery();

        return resultSet.next();
    }

    public RegistrationDto getUserInfo(String userName) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE user_name = ?";
        try (PreparedStatement ptsm = connection.prepareStatement(sql)){
            ptsm.setString(1,userName);

            try (ResultSet resultSet = ptsm.executeQuery()){
                if (resultSet.next()){
                    String retrievedUserName = resultSet.getString("user_name");
                    String retrievedPassword = resultSet.getString("password");

                    return new RegistrationDto(retrievedUserName,retrievedPassword);
                }
            }
        }
        return null;
    }

    public boolean check(String userName, String password) throws SQLException {
        return isValidUser(userName,password);
    }

    public boolean registerUser(RegistrationDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO user VALUES(?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,dto.getUserName());
        ptsm.setString(2,dto.getPassword());

        return ptsm.executeUpdate() > 0;
    }
}
