package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.RegistrationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationModel {
    public boolean save(RegistrationDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO user VALUES (?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getUserName());
        ptsm.setString(2, dto.getPassword());
        ptsm.setInt(3, dto.getPhone_number());

        return ptsm.executeUpdate() > 0;
    }

    public boolean isValidUser(String userName, String password) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE user_name = ? AND password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, userName);
        ptsm.setString(2, password);

        ResultSet resultSet = ptsm.executeQuery();

        return resultSet.next();
    }

    public RegistrationDto getUserInfo(String userName) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE user_name = ?";
        try (PreparedStatement ptsm = connection.prepareStatement(sql)) {
            ptsm.setString(1, userName);

            try (ResultSet resultSet = ptsm.executeQuery()) {
                if (resultSet.next()) {
                    String retrievedUserName = resultSet.getString("user_name");
                    String retrievedPassword = resultSet.getString("password");
                    int retrievedPhoneNumber = resultSet.getInt("phone_number");

                    return new RegistrationDto(retrievedUserName, retrievedPassword, retrievedPhoneNumber);
                }
            }
        }
        return null;
    }
    public boolean check(int phoneNumber) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT  COUNT(*)FROM user WHERE phone_number = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setInt(1, phoneNumber);

        ResultSet resultSet = ptsm.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        } else {
            return false;
        }
    }
}
