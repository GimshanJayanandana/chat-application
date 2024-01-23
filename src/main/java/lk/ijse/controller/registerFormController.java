package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dto.RegistrationDto;
import lk.ijse.model.RegistrationModel;

import java.io.IOException;
import java.sql.SQLException;

public class registerFormController {

    @FXML
    private TextField txtNewPassword;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private AnchorPane registerPane;

    private RegistrationModel registrationModel = new RegistrationModel();

    private void clearFields(){
        txtPassword.setText("");
        txtNewPassword.setText("");
        txtUserName.setText("");
    }
    @FXML
    void btnRegisterOnAction(ActionEvent event) throws SQLException {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtNewPassword.getText();

        var dto = new RegistrationDto(userName,password);
        boolean checkDuplicates = registrationModel.check(userName, password);
        if (checkDuplicates) {
            new Alert(Alert.AlertType.ERROR, "Duplicate Entry").showAndWait();
            return;
        }
        boolean isRegistered = registrationModel.registerUser(dto);
        if (isRegistered) {
            new Alert(Alert.AlertType.CONFIRMATION, "Your Account Has Been Created").show();
            clearFields();
        }
    }

    @FXML
    void txtPasswordGoToNewPasswordOnAction(ActionEvent event) {
        txtNewPassword.requestFocus();
    }
    @FXML
    void txtUserNameGoToPasswordOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }
    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/loginForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.registerPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.centerOnScreen();

    }
    @FXML
    void txtConfirmPasswordGoToRegisterOnAction(ActionEvent event) throws SQLException {
        btnRegisterOnAction(new ActionEvent());
    }

}