package org.lab5.lab5;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Магазин игрушек");

        // Установка иконки приложения
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/lab5/lab5/icon.png")));
        } catch (Exception e) {
            System.out.println("Иконка не найдена");
        }

        // Показываем окно входа
        showLoginView();

        primaryStage.show();

        primaryStage.setMaximized(true); //////////
    }

    public static void showLoginView() {
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 900, 600);
        scene.getStylesheets().add(MainApp.class.getResource("/org/lab5/lab5/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1100); //
        primaryStage.setMinHeight(700); //
        primaryStage.setMaximized(true);
    }

    public static void showAdminView() {
        AdminView adminView = new AdminView();
        Scene scene = new Scene(adminView.getView(), 900, 600);
        scene.getStylesheets().add(MainApp.class.getResource("/org/lab5/lab5/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900); //
        primaryStage.setMinHeight(600); //
        primaryStage.setMaximized(true);
    }

    public static void showCustomerView(Customer customer) {
        CustomerView customerView = new CustomerView(customer);
        Scene scene = new Scene(customerView.getView(), 1000, 700);
        scene.getStylesheets().add(MainApp.class.getResource("/org/lab5/lab5/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000); //
        primaryStage.setMinHeight(700); //
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
