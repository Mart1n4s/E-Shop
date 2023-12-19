package com.project.shop.fxControllers;

import com.project.shop.StartGui;
import com.project.shop.hibernateControllers.GenericHib;
import com.project.shop.model.Product;
import com.project.shop.model.Review;
import com.project.shop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;

public class ReviewController {
    @FXML
    public ListView<Review> reviewList;
    @FXML
    public TextField reviewRating;
    @FXML
    public TextArea reviewBody;
    @FXML
    public TextField reviewTitle;
    @FXML
    public TextArea productField;
    @FXML
    public Button goBackBtn;

    GenericHib genericHib;
    EntityManagerFactory entityManagerFactory;
    Product product;
    User currentUser;

    public void setData(EntityManagerFactory entityManagerFactory, Product product, User currentUser){
        this.entityManagerFactory = entityManagerFactory;
        this.product = product;
        this.currentUser = currentUser;
        this.genericHib = new GenericHib(entityManagerFactory);
        loadReviews();
    }

    public void loadReviews() {
        reviewList.getItems().clear();
        Collection<Review> reviewsCollection = product.getReviews();
        ObservableList<Review> reviewsList = FXCollections.observableArrayList(reviewsCollection);
        reviewList.setItems(reviewsList);
        productField.setText(String.valueOf(product));
        productField.setEditable(false);
    }

    public void loadReview() {
        Review selectedReview = reviewList.getSelectionModel().getSelectedItem();
        reviewTitle.setText(selectedReview.getCommentTitle());
        reviewBody.setText(selectedReview.getCommentBody());
        reviewRating.setText(String.valueOf(selectedReview.getRating()));
    }

    public void createReview() {
        Review review = new Review(reviewTitle.getText(), reviewBody.getText(), Double.parseDouble(reviewRating.getText()), product);
        genericHib.create(review);
        reviewList.getItems().add(review);
    }

    public void updateReview() {
        Review selectedReview = reviewList.getSelectionModel().getSelectedItem();
        Review review = genericHib.getEntityById(Review.class, selectedReview.getId());
        review.setCommentTitle(reviewTitle.getText());
        review.setCommentBody(reviewBody.getText());
        genericHib.update(review);
        reviewList.getItems().set(reviewList.getItems().indexOf(selectedReview), review);
    }

    public void deleteReview() {
        Review selectedReview = reviewList.getSelectionModel().getSelectedItem();
        if (selectedReview != null) {
            genericHib.deleteComment(selectedReview.getId());
            reviewList.getItems().remove(selectedReview);
        }
    }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-shop.fxml"));
        Parent parent = fxmlLoader.load();
        MainShopController mainShopController = fxmlLoader.getController();
        mainShopController.setData(entityManagerFactory, currentUser);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) goBackBtn.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

}
