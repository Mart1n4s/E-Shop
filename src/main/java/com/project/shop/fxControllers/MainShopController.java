package com.project.shop.fxControllers;

import com.project.shop.StartGui;
import com.project.shop.fxControllers.alerts.Alerts;
import com.project.shop.hibernateControllers.GenericHib;
import com.project.shop.hibernateControllers.UserHib;
import com.project.shop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.shop.fxControllers.hashPassword.HashPassword.hashPassword;

public class MainShopController implements Initializable {

    //-------------------------General Util-----------------------//

    @FXML
    public Button goToPaymentBtn;
    @FXML
    public ListView<Product> productList;
    @FXML
    public ListView<Product> currentOrder;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab customerOrderTab;
    @FXML
    public Tab ordersTab;
    @FXML
    public Tab warehouseTab;
    @FXML
    public Tab productTab;
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab primaryTab;

    //-------------------------Warehouse Tab-----------------------//

    @FXML
    public ListView<Warehouse> warehouseList;
    @FXML
    public TextField addressWarehouseField;
    @FXML
    public TextField titleWarehouseField;

    //-------------------------Product Manager Tab------------------//
    @FXML
    public ListView<Object> productListManager;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea productDescriptionField;
    @FXML
    public ComboBox<ProductType> productType;
    @FXML
    public ComboBox<Warehouse> warehouseComboBox;
    @FXML
    public DatePicker productExpiryDate;
    @FXML
    public TextField productSizeField;
    @FXML
    public TextField productColorField;
    @FXML
    public TextField productWeightField;
    @FXML
    public TextField productManufacturerField;
    @FXML
    public TextField productPriceField;

    //----------------------------Comment tree--------------------------------//

    @FXML
    private TreeView<Comment> commentsTree;
    @FXML
    private Button addReviewBtn;
    @FXML
    private TextArea replyField;

    //-----------------------------------General orders--------------------------//

    @FXML
    public ComboBox<OrderStatus> orderStatus;
    @FXML
    public ComboBox<Manager> orderEmployee;
    @FXML
    public TextField orderDate;
    @FXML
    public ListView<Cart> orderList;
    @FXML
    public ListView<Comment> orderChat;
    @FXML
    public TextField orderChatField;

    @FXML
    public CheckBox statusCheckBox;

    @FXML
    public CheckBox priceCheckBox;

    @FXML
    public DatePicker datePicker;

    @FXML
    public ToggleGroup userType;

    //------------------------------------Customer Orders-----------------------------//
    @FXML
    public ListView<Cart> myOrdersList;
    @FXML
    public TextField myOrderStatus;
    @FXML
    public ListView<Comment> customerOrderChat;
    @FXML
    public TextField customerOrderChatField;

    //------------------------------------Customer Orders History Tab-----------------------------//
    @FXML
    public ListView<Cart> customerOrderHistory;
    @FXML
    public Tab customerOrderHistoryTab;

    //------------------------------------Statistics Tab-----------------------------//

    @FXML
    public Tab statisticsTab;
    @FXML
    public LineChart<String, Number> graph;


    //------------------------------------User Tab-----------------------------//

    @FXML
    public Tab userTab;
    @FXML
    public ListView userList;
    @FXML
    public RadioButton customerCheckBox;
    @FXML
    public RadioButton managerCheckBox;
    @FXML
    public TextField loginField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public DatePicker birthdateField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField cardNumberField;
    @FXML
    public TextField medCertificateField;
    @FXML
    public TextField employeeIdField;
    @FXML
    public DatePicker employmentField;
    @FXML
    public CheckBox isAdminField;

    //-------------------------------------General----------------------------------//

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private GenericHib genericHib;
    private UserHib userHib;


    //---------------------------------Settings-------------------------------//
    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        limitTabAccess();
        loadMainProductList();
    }

    private void loadMainProductList() {
        genericHib = new GenericHib(entityManagerFactory);
        userHib = new UserHib(entityManagerFactory);
        productList.getItems().clear();
        productList.getItems().addAll(genericHib.getAllRecords(Product.class));
    }


    //---------------------------------User access-------------------------------//
    private void limitTabAccess() {
        if (currentUser.getClass() == Manager.class) {
            Manager manager = (Manager) currentUser;
            tabPane.getTabs().remove(customerOrderTab);
            tabPane.getTabs().remove(customerOrderHistoryTab);
            if (!manager.isAdmin()) {
                userTab.setDisable(true);
            }
        } else if (currentUser.getClass() == Customer.class) {
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(warehouseTab);
            tabPane.getTabs().remove(productTab);
            tabPane.getTabs().remove(ordersTab);
            tabPane.getTabs().remove(statisticsTab);
            tabPane.getTabs().remove(userTab);
        }
    }

    public void loadTabValues() {
        if (productTab.isSelected()) {
            loadProductListManager();
        } else if (warehouseTab.isSelected()) {
            loadWarehouseList();
        }  else if (primaryTab.isSelected()) {
            loadMainProductList();
        } else if (customerOrderTab.isSelected()) {
            loadCustomerOrders();
        } else if (ordersTab.isSelected()) {
            loadOrders();
        } else if (customerOrderHistoryTab.isSelected()) {
            loadCustomerOrderHistory();
        } else if (statisticsTab.isSelected()) {
            loadStatistics();
        } else if (userTab.isSelected()) {
            loadUsers();
        }
    }

    //---------------------------------Users-------------------------------//

    public void loadUsers() {
        EventHandler<ActionEvent> checkboxHandler = event -> showUserList();
        customerCheckBox.setOnAction(checkboxHandler);
        managerCheckBox.setOnAction(checkboxHandler);
    }

    private void showUserList() {
        if (customerCheckBox.isSelected()) {
            List<Customer> users = genericHib.getAllRecords(Customer.class);
            ObservableList<Customer> observableUsers = FXCollections.observableArrayList(users);
            userList.getItems().clear();
            userList.setItems(observableUsers);

            employeeIdField.setVisible(false);
            employmentField.setVisible(false);
            isAdminField.setVisible(false);
            medCertificateField.setVisible(false);
            addressField.setDisable(false);
            cardNumberField.setDisable(false);

        } else {
            List<Manager> users = genericHib.getAllRecords(Manager.class);
            ObservableList<Manager> observableUsers = FXCollections.observableArrayList(users);
            userList.getItems().clear();
            userList.setItems(observableUsers);

            addressField.setDisable(true);
            cardNumberField.setDisable(true);
            employeeIdField.setVisible(true);
            employmentField.setVisible(true);
            isAdminField.setVisible(true);
            medCertificateField.setVisible(true);
        }
    }

    public void loadUserInfo() {
        if(customerCheckBox.isSelected()){
            Customer selectedUser = (Customer) userList.getSelectionModel().getSelectedItem();
            loginField.setText(selectedUser.getLogin());
            passwordField.setText(selectedUser.getPassword());
            nameField.setText(selectedUser.getName());
            surnameField.setText(selectedUser.getSurname());
            birthdateField.setValue(selectedUser.getBirthDate());
            addressField.setText(selectedUser.getAddress());
            cardNumberField.setText(selectedUser.getCardNo());
        } else {
            Manager selectedUser = (Manager) userList.getSelectionModel().getSelectedItem();
            loginField.setText(selectedUser.getLogin());
            passwordField.setText(selectedUser.getPassword());
            nameField.setText(selectedUser.getName());
            surnameField.setText(selectedUser.getSurname());
            birthdateField.setValue(selectedUser.getBirthDate());
            employeeIdField.setText(selectedUser.getEmployeeId());
            employmentField.setValue(selectedUser.getEmploymentDate());
            isAdminField.setSelected(selectedUser.isAdmin());
            medCertificateField.setText(selectedUser.getMedCertificate());
        }
    }

    public void addUser(){
        if(customerCheckBox.isSelected()){
            Customer customer = new Customer(
                    loginField.getText(),
                    hashPassword(passwordField.getText()),
                    birthdateField.getValue(),
                    nameField.getText(),
                    surnameField.getText(),
                    addressField.getText(),
                    cardNumberField.getText()
            );
            genericHib.create(customer);
        } else {
            Manager manager = new Manager(
                    loginField.getText(),
                    hashPassword(passwordField.getText()),
                    birthdateField.getValue(),
                    nameField.getText(),
                    surnameField.getText(),
                    employeeIdField.getText(),
                    medCertificateField.getText(),
                    employmentField.getValue(),
                    isAdminField.isSelected()

            );
            genericHib.create(manager);
        }
        showUserList();
    }

    public void updateUser(){
        if(customerCheckBox.isSelected()){
            Customer selectedUser = (Customer) userList.getSelectionModel().getSelectedItem();
            selectedUser.setLogin(loginField.getText());
            selectedUser.setPassword(hashPassword(passwordField.getText()));
            selectedUser.setName(nameField.getText());
            selectedUser.setSurname(surnameField.getText());
            selectedUser.setBirthDate(birthdateField.getValue());
            selectedUser.setAddress(addressField.getText());
            selectedUser.setCardNo(cardNumberField.getText());
            userHib.updateUser(selectedUser);
        } else {
            Manager selectedUser = (Manager) userList.getSelectionModel().getSelectedItem();
            selectedUser.setLogin(loginField.getText());
            selectedUser.setPassword(hashPassword(passwordField.getText()));
            selectedUser.setName(nameField.getText());
            selectedUser.setSurname(surnameField.getText());
            selectedUser.setBirthDate(birthdateField.getValue());
            selectedUser.setEmployeeId(employeeIdField.getText());
            selectedUser.setEmploymentDate(employmentField.getValue());
            selectedUser.setAdmin(isAdminField.isSelected());
            selectedUser.setMedCertificate(medCertificateField.getText());
            userHib.updateUser(selectedUser);
        }
    }

    public void deleteUser(){
        User user = (User) userList.getSelectionModel().getSelectedItem();
        userHib.deleteUser(user.getId());
        showUserList();
    }

    //---------------------------------Statistics-------------------------------//

    public void loadStatistics() {
        List<Cart> carts = genericHib.getAllRecords(Cart.class);
        graph.setAnimated(false);
        graph.getData().clear();
        graph.setTitle("Shop statistics");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Amount of products in cart");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<XYChart.Data<String, Number>> sortedData = new ArrayList<>();


        for (Cart cart : carts) {
            LocalDate dateCreated = cart.getDateCreated();
            if (dateCreated != null) {
                String dateString = dateCreated.format(dateFormatter);
                sortedData.add(new XYChart.Data<>(dateString, cart.getItemsInCart().size()));
            }
        }
        sortedData.sort(Comparator.comparing(XYChart.Data::getXValue));

        series.getData().addAll(sortedData);
        graph.getData().add(series);
    }

    //----------------------------Customer orders history-----------------------------//

    public void loadCustomerOrderHistory() {
        List<Cart> completedCarts = currentUser.getMyCarts().stream()
                .filter(cart -> OrderStatus.COMPLETED.equals(cart.getOrderStatus()))
                .collect(Collectors.toList());

        ObservableList<Cart> observableCompletedCarts = FXCollections.observableArrayList(completedCarts);
        customerOrderHistory.setItems(observableCompletedCarts);
    }

    //----------------------------Customer orders-----------------------------//

    public void loadCustomerOrders() {
        myOrdersList.getItems().clear();
        List<Cart> userCarts = currentUser.getMyCarts()
                .stream()
                .filter(cart -> cart.getOrderStatus() != OrderStatus.COMPLETED)
                .collect(Collectors.toList());

        ObservableList<Cart> observableUserCarts = FXCollections.observableArrayList(userCarts);
        myOrdersList.setItems(observableUserCarts);
    }

    public void loadCustomerOrderInfo() {
        loadCustomerOrderStatus();
        loadCustomerChat();
    }

    public void loadCustomerOrderStatus() {
        Cart selectedCart = myOrdersList.getSelectionModel().getSelectedItem();
        myOrderStatus.setEditable(false);
        myOrderStatus.setText(String.valueOf(selectedCart.getOrderStatus()));
    }

    public void addCustomerOrderComment() {
        Comment comment = new Comment("C: " + customerOrderChatField.getText());
        Cart selectedCart = myOrdersList.getSelectionModel().getSelectedItem();
        selectedCart.getOrderComments().add(comment);
        customerOrderChat.getItems().clear();
        customerOrderChat.getItems().addAll(selectedCart.getOrderComments());
        genericHib.update(selectedCart);
    }

    public void loadCustomerChat() {
        Cart selectedCart = myOrdersList.getSelectionModel().getSelectedItem();
        if (selectedCart != null) {
            List<Comment> orderCustomerChat = new ArrayList<>(selectedCart.getOrderComments());
            ObservableList<Comment> observableOrderChat = FXCollections.observableArrayList(orderCustomerChat);
            customerOrderChat.setItems(observableOrderChat);
        }
    }



    //-----------------------------------All orders-----------------------------//

    public void loadOrders() {
        List<Cart> userCarts = genericHib.getAllRecords(Cart.class);
        userCarts.sort(Comparator.comparing(Cart::getDateCreated));
        ObservableList<Cart> observableUserCarts = FXCollections.observableArrayList(userCarts);
        orderList.setCellFactory(param -> new ListCell<Cart>() {
            @Override
            protected void updateItem(Cart cart, boolean empty) {
                super.updateItem(cart, empty);

                if (empty || cart == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(cart.toString());
                    if(isPrepared(cart))
                    {
                        setStyle("-fx-background-color: red;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        orderList.setItems(observableUserCarts);
    }

    private boolean isPrepared(Cart cart) {
        return cart.getDateCreated().plusDays(1).isBefore(LocalDate.now()) && cart.getOrderStatus() != OrderStatus.COMPLETED;
    }

    public void filterByStatus() {
        if (statusCheckBox.isSelected()) {
            List<Cart> userCarts = genericHib.getAllRecords(Cart.class)
                    .stream()
                    .filter(cart -> cart.getOrderStatus() != OrderStatus.COMPLETED)
                    .sorted(Comparator.comparing(Cart::getOrderStatus))
                    .collect(Collectors.toList());
            ObservableList<Cart> observableUserCarts = FXCollections.observableArrayList(userCarts);
            orderList.setItems(observableUserCarts);
        } else {
            loadOrders();
        }
    }

    public void filterByPrice() {
        if (priceCheckBox.isSelected()) {
            List<Cart> userCarts = genericHib.getAllRecords(Cart.class)
                    .stream()
                    .sorted(Comparator.comparing(cart -> calculateTotalWorth(cart.getItemsInCart())))
                    .collect(Collectors.toList());

            ObservableList<Cart> observableUserCarts = FXCollections.observableArrayList(userCarts);
            orderList.setItems(observableUserCarts);
        } else {
            loadOrders();
        }
    }

    private double calculateTotalWorth(List<Product> products) {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public void filterByDate() {
        if (datePicker.getValue() != null) {
            LocalDate selectedDate = datePicker.getValue();
            List<Cart> userCarts = genericHib.getAllRecords(Cart.class)
                    .stream()
                    .filter(cart -> cart.getDateCreated().isBefore(selectedDate))
                    .sorted(Comparator.comparing(Cart::getDateCreated))
                    .collect(Collectors.toList());

            ObservableList<Cart> observableUserCarts = FXCollections.observableArrayList(userCarts);
            orderList.setItems(observableUserCarts);
        } else {
            loadOrders();
        }
    }

    public void loadOrderInfo() {
        Cart selectedCart = orderList.getSelectionModel().getSelectedItem();
        orderDate.setEditable(false);
        orderStatus.setValue(selectedCart.getOrderStatus());
        orderEmployee.setValue(selectedCart.getEmployee());
        orderDate.setText(String.valueOf(selectedCart.getDateCreated()));

        loadEmployee();
        loadChat();
    }

    public void addOrderComment() {
        Comment comment = new Comment("E: " + orderChatField.getText());
        Cart selectedCart = orderList.getSelectionModel().getSelectedItem();
        selectedCart.getOrderComments().add(comment);
        orderChat.getItems().clear();
        orderChat.getItems().addAll(selectedCart.getOrderComments());
        genericHib.update(selectedCart);
    }

    public void loadChat() {
        Cart selectedCart = orderList.getSelectionModel().getSelectedItem();
        if (selectedCart != null) {
            List<Comment> orderCustomerChat = new ArrayList<>(selectedCart.getOrderComments());
            ObservableList<Comment> observableOrderChat = FXCollections.observableArrayList(orderCustomerChat);
            orderChat.setItems(observableOrderChat);
        }
    }

    public void loadStatus() {
        orderStatus.getItems().clear();
        orderStatus.getItems().addAll(OrderStatus.values());
    }

    public void updateOrderStatus() {
        Cart selectedCart = orderList.getSelectionModel().getSelectedItem();
        orderStatus.setValue(orderStatus.getValue());
        selectedCart.setOrderStatus(orderStatus.getValue());
        genericHib.update(selectedCart);
    }

    public void loadEmployee() {
        orderEmployee.getItems().clear();
        List<Manager> orderEmployeeList = genericHib.getAllRecords(Manager.class);
        orderEmployee.getItems().addAll(orderEmployeeList);
    }

    public void updateOrderEmployee() {
        Cart selectedCart = orderList.getSelectionModel().getSelectedItem();
        orderEmployee.setValue(orderEmployee.getValue());
        selectedCart.setEmployee(orderEmployee.getValue());
        genericHib.update(selectedCart);

    }

    //------------------------------------Cart-------------------------------------//

    public void addToCart(){
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if(selectedProduct == null) return;
        ObservableList<Product>  orderProducts = currentOrder.getItems();
        orderProducts.add(selectedProduct);
        currentOrder.setItems(orderProducts);
        productList.getItems().remove(selectedProduct);
    }

    public void deleteFromCart() {
        Product selectecProduct = currentOrder.getSelectionModel().getSelectedItem();
        if(selectecProduct == null) return;
        currentOrder.getItems().remove(selectecProduct);
        ObservableList<Product> products = productList.getItems();
        products.add(selectecProduct);
        productList.setItems(products);
    }

    public void goToPayment() throws IOException {
        if(currentUser instanceof Customer) {
            ObservableList<Product> orderProducts = currentOrder.getItems();
            Cart cart = createCart(orderProducts);

            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("payment.fxml"));
            Parent parent = fxmlLoader.load();
            PaymentController paymentController = fxmlLoader.getController();
            paymentController.setData(entityManagerFactory, (Customer) currentUser, cart);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) goToPaymentBtn.getScene().getWindow();
            stage.setTitle("Shop");
            stage.setScene(scene);
            stage.show();
        } else {
            Alerts.generateAlert(Alert.AlertType.INFORMATION, "Payment INFO", "Wrong user", "Only customer can go to payment");
        }
    }

    private Cart createCart(ObservableList<Product> orderProducts) {
        Cart cart = new Cart(currentUser, OrderStatus.PENDING, orderProducts);
        return cart;
    }
    //---------------------------------Product Manager -------------------------------//

    public void enableProductFields() {
        if (productType.getSelectionModel().getSelectedItem() == ProductType.FOOD) {
            productExpiryDate.setDisable(false);
            productWeightField.setDisable(true);
            productSizeField.setDisable(true);
            productColorField.setDisable(true);
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.OTHER) {
            productExpiryDate.setDisable(true);
            productWeightField.setDisable(false);
            productSizeField.setDisable(true);
            productColorField.setDisable(true);
        } else {
            productExpiryDate.setDisable(true);
            productWeightField.setDisable(true);
            productSizeField.setDisable(false);
            productColorField.setDisable(false);
        }
    }
    private void loadProductListManager() {
        List<Warehouse> warehouses = genericHib.getAllRecords(Warehouse.class);
        warehouseComboBox.getItems().clear();
        warehouseComboBox.getItems().addAll(warehouses);
        productListManager.getItems().clear();
        productListManager.getItems().addAll(genericHib.getAllRecords(Product.class));
    }

    public void addNewProductListManager() {
        Warehouse selectedWarehouse = warehouseComboBox.getSelectionModel().getSelectedItem();
        Warehouse warehouse = genericHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        if (productType.getSelectionModel().getSelectedItem() == ProductType.FOOD) {
                genericHib.create(new Food(productTitleField.getText(), productDescriptionField.getText(),
                        productManufacturerField.getText(), warehouse, productExpiryDate.getValue(), productType.getValue(),
                        Double.parseDouble(productPriceField.getText())));
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.CLOTHES) {
                genericHib.create(new Clothes(productTitleField.getText(), productDescriptionField.getText(),
                        productManufacturerField.getText(), warehouse, productSizeField.getText(), productColorField.getText(),
                        productType.getValue(), Double.parseDouble(productPriceField.getText())));
        } else {
                genericHib.create(new Other(productTitleField.getText(), productDescriptionField.getText(),
                        productManufacturerField.getText(), warehouse, Double.parseDouble(productWeightField.getText()),
                        productType.getValue(), Double.parseDouble(productPriceField.getText())));

        }
        clearProductFields();
        loadProductListManager();
    }

    public void updateProductListManager() {
        Object obj = productListManager.getSelectionModel().getSelectedItem();
        if(obj == null){
            return;
        }
        if(obj.getClass() == Food.class){
            Food selectedProduct = (Food) obj;
            selectedProduct.setTitle(productTitleField.getText());
            selectedProduct.setDescription(productDescriptionField.getText());
            selectedProduct.setManufacturer(productManufacturerField.getText());
            selectedProduct.setWarehouse(warehouseComboBox.getValue());
            selectedProduct.setExpiryDate(productExpiryDate.getValue());
            selectedProduct.setProductType(productType.getValue());
            selectedProduct.setPrice((Double.parseDouble(productPriceField.getText())));
            genericHib.update(selectedProduct);
        } else if (obj.getClass() == Clothes.class){
            Clothes selectedProduct = (Clothes) obj;
            selectedProduct.setTitle(productTitleField.getText());
            selectedProduct.setDescription(productDescriptionField.getText());
            selectedProduct.setManufacturer(productManufacturerField.getText());
            selectedProduct.setWarehouse(warehouseComboBox.getValue());
            selectedProduct.setProductType(productType.getValue());
            selectedProduct.setSize(productSizeField.getText());
            selectedProduct.setColor(productColorField.getText());
            selectedProduct.setPrice((Double.parseDouble(productPriceField.getText())));
            genericHib.update(selectedProduct);
        } else {
            Other selectedProduct = (Other) obj;
            selectedProduct.setTitle(productTitleField.getText());
            selectedProduct.setDescription(productDescriptionField.getText());
            selectedProduct.setManufacturer(productManufacturerField.getText());
            selectedProduct.setWarehouse(warehouseComboBox.getValue());
            selectedProduct.setProductType(productType.getValue());
            selectedProduct.setWeight(Double.parseDouble(productWeightField.getText()));
            selectedProduct.setPrice((Double.parseDouble(productPriceField.getText())));
            genericHib.update(selectedProduct);
        }
        setProductListManagerValues();
    }
    public void deleteProductListManager() {
        Product selectedProduct = (Product) productListManager.getSelectionModel().getSelectedItem();
        selectedProduct.setWarehouse(null);
        genericHib.update(selectedProduct);
        genericHib.delete(Product.class, selectedProduct.getId());
        loadProductListManager();
    }
    public void clearProductFields(){
        productTitleField.setText("");
        productDescriptionField.setText("");
        productManufacturerField.setText("");
        warehouseComboBox.setValue(null);
        productType.setValue(null);
        productExpiryDate.setValue(null);
        productSizeField.setText("");
        productColorField.setText("");
        productWeightField.setText("");
        productPriceField.setText("");
    }

    public void setProductListManagerValues() {
        productListManager.refresh();
        Object obj = productListManager.getSelectionModel().getSelectedItem();
        if(obj == null){
            return;
        }
        clearProductFields();
        if(obj.getClass() == Food.class){
            Food selectedProduct = (Food) obj;
            productTitleField.setText(selectedProduct.getTitle());
            productDescriptionField.setText(selectedProduct.getDescription());
            productManufacturerField.setText(selectedProduct.getManufacturer());
            warehouseComboBox.setValue(selectedProduct.getWarehouse());
            productExpiryDate.setValue((selectedProduct.getExpiryDate()));
            productType.setValue(selectedProduct.getProductType());
            productPriceField.setText(String.valueOf(selectedProduct.getPrice()));
        } else if (obj.getClass() == Clothes.class){
            Clothes selectedProduct = (Clothes) obj;
            productTitleField.setText(selectedProduct.getTitle());
            productDescriptionField.setText(selectedProduct.getDescription());
            productManufacturerField.setText(selectedProduct.getManufacturer());
            warehouseComboBox.setValue(selectedProduct.getWarehouse());
            productType.setValue(selectedProduct.getProductType());
            productSizeField.setText(selectedProduct.getSize());
            productColorField.setText(selectedProduct.getColor());
            productPriceField.setText(String.valueOf(selectedProduct.getPrice()));
        } else {
            Other selectedProduct = (Other) obj;
            productTitleField.setText(selectedProduct.getTitle());
            productDescriptionField.setText(selectedProduct.getDescription());
            productManufacturerField.setText(selectedProduct.getManufacturer());
            warehouseComboBox.setValue(selectedProduct.getWarehouse());
            productType.setValue(selectedProduct.getProductType());
            productWeightField.setText(String.valueOf(selectedProduct.getWeight()));
            productPriceField.setText(String.valueOf(selectedProduct.getPrice()));
        }
    }

    //------------------------------Warehouse-----------------------------//

    private void loadWarehouseList() {
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(genericHib.getAllRecords(Warehouse.class));
    }

    public void addNewWarehouse() {
        Warehouse warehouse = new Warehouse(titleWarehouseField.getText(), addressWarehouseField.getText());
        genericHib.create(warehouse);
        loadWarehouseList();
    }

    public void updateWarehouse() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        Warehouse warehouse = genericHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        warehouse.setTitle(titleWarehouseField.getText());
        warehouse.setAddress(addressWarehouseField.getText());
        genericHib.update(warehouse);
        loadWarehouseList();
    }

    public void removeWarehouse() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        genericHib.delete(Warehouse.class, selectedWarehouse.getId());
        loadWarehouseList();
    }

    public void loadWarehouseData() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        titleWarehouseField.setText(selectedWarehouse.getTitle());
        addressWarehouseField.setText(selectedWarehouse.getAddress());
    }

    //---------------------------------Comments-----------------------------//
    public void loadComments() {
        Product selectedProduct = genericHib.getEntityById(Product.class, productList.getSelectionModel().getSelectedItem().getId());
        commentsTree.setRoot(new TreeItem<>());
        commentsTree.setShowRoot(false);
        commentsTree.getRoot().setExpanded(true);
        selectedProduct.getReviews().forEach(comment -> addTreeItem(comment, commentsTree.getRoot()));
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        comment.getReplies().forEach(subcomment -> addTreeItem(subcomment, treeItem));
    }

    public void addReview() throws IOException {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("review.fxml"));
        Parent parent = fxmlLoader.load();
        ReviewController reviewController = fxmlLoader.getController();
        reviewController.setData(entityManagerFactory, selectedProduct, currentUser);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) addReviewBtn.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void addReply() {
        TreeItem<Comment> selectedTreeItem = commentsTree.getSelectionModel().getSelectedItem();
        if (selectedTreeItem != null) {
            Comment selectedComment = selectedTreeItem.getValue();
            String replyText = replyField.getText();
            if (selectedComment != null && replyText != null && !replyText.isEmpty()) {
                Comment reply = new Comment(replyText, selectedComment);
                genericHib.create(reply);
                addTreeItem(reply, selectedTreeItem);
                loadComments();
            }
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources){
        productType.getItems().addAll(ProductType.values());
    }

}
