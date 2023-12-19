package com.project.shop.fxControllers;

import com.project.shop.StartGui;
import com.project.shop.fxControllers.alerts.Alerts;
import com.project.shop.hibernateControllers.UserHib;
import com.project.shop.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.project.shop.fxControllers.hashPassword.HashPassword.hashPassword;


public class LoginController implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;

    public void registerNewUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void validateAndConnect() throws IOException {
        userHib = new UserHib(entityManagerFactory);
        String hashedPassword = hashPassword(passwordField.getText());
        User user = userHib.getUserByCredentials(loginField.getText(), hashedPassword);
        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-shop.fxml"));
            Parent parent = fxmlLoader.load();
            MainShopController mainShopController = fxmlLoader.getController();
            mainShopController.setData(entityManagerFactory, user);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("Shop");
            stage.setScene(scene);
            stage.show();
        } else {
            Alerts.generateAlert(Alert.AlertType.INFORMATION, "Login INFO", "Wrong data", "Please check credentials, no such user");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManagerFactory = Persistence.createEntityManagerFactory("coursework-shop");
    }
}
