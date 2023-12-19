package com.project.shop.fxControllers;


import com.project.shop.StartGui;
import com.project.shop.hibernateControllers.GenericHib;
import com.project.shop.model.Cart;
import com.project.shop.model.Customer;
import com.project.shop.model.Product;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    public Button cancelPaymentBtn;
    @FXML
    public Button confirmPaymentBtn;
    @FXML
    public ListView<Product> paymentProductList;
    @FXML
    public TextField cardNoField;
    @FXML
    public TextField priceField;

    private Customer currentUser;
    private EntityManagerFactory entityManagerFactory;
    private Cart cart;
    private GenericHib genericHib;

    public void setData(EntityManagerFactory entityManagerFactory, Customer currentUser, Cart cart) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        this.cart = cart;
        this.genericHib = new GenericHib(entityManagerFactory);
        loadPaymentProductList();
        loadOtherInformation();
    }

    public void loadPaymentProductList() {
        paymentProductList.setItems((ObservableList<Product>) cart.getItemsInCart());
    }

    public void loadOtherInformation() {
        cardNoField.setEditable(false);
        cardNoField.setText(currentUser.getCardNo());

        List<Product> productList = paymentProductList.getItems();

        double price = 0.0;
        for(Product product : productList) {
            price+= product.getPrice();
        }

        priceField.setEditable(false);
        priceField.setText(String.valueOf(price));
    }

    public void cancelPayment() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-shop.fxml"));
        Parent parent = fxmlLoader.load();
        MainShopController mainShopController = fxmlLoader.getController();
        mainShopController.setData(entityManagerFactory, currentUser);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) cancelPaymentBtn.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void confirmPayment() throws IOException {
        genericHib.create(cart);

        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-shop.fxml"));
        Parent parent = fxmlLoader.load();
        MainShopController mainShopController = fxmlLoader.getController();
        mainShopController.setData(entityManagerFactory, currentUser);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) confirmPaymentBtn.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
