module com.kursinis.prif4kursinis {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires jbcrypt;


    opens com.project.shop to javafx.fxml;
    exports com.project.shop;
    opens com.project.shop.model to javafx.fxml, org.hibernate.orm.core;
    exports com.project.shop.model;
    opens com.project.shop.fxControllers to javafx.fxml;
    exports com.project.shop.fxControllers to javafx.fxml;
    exports com.project.shop.fxControllers.alerts to javafx.fxml;
    opens com.project.shop.fxControllers.alerts to javafx.fxml;
}