package com.project.shop.fxControllers;

import com.project.shop.StartGui;
import com.project.shop.fxControllers.alerts.Alerts;
import com.project.shop.hibernateControllers.UserHib;
import com.project.shop.model.Customer;
import com.project.shop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.project.shop.fxControllers.hashPassword.HashPassword.hashPassword;

public class RegistrationController implements Initializable {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField cardNoField;
    @FXML
    public DatePicker birthDateField;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;


    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void returnToStart() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("login.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void createUser() {
        userHib = new UserHib(entityManagerFactory);

        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty() || surnameField.getText().isEmpty()) {
            Alerts.generateAlert(Alert.AlertType.INFORMATION, "Register INFO", "Missing data", "Please check if login, password, name and surname fields are filled");
        } else if (!Objects.equals(passwordField.getText(), repeatPasswordField.getText())) {
            Alerts.generateAlert(Alert.AlertType.INFORMATION, "Register INFO", "Passwords don't match", "Be sure that passwords matches");
        } else {
            User user = getUser();
            userHib.createUser(user);
            Alerts.generateAlert(Alert.AlertType.INFORMATION, "Register INFO", "User", "User is successfully created");
        }
    }

    private User getUser() {
        User user = new Customer(
                loginField.getText(),
                hashPassword(passwordField.getText()),
                birthDateField.getValue(),
                nameField.getText(),
                surnameField.getText(),
                addressField.getText(),
                cardNoField.getText()
        );
        return user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
