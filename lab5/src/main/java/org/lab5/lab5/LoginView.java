package org.lab5.lab5;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView {

    private BorderPane root;
    private TextField loginField;
    private PasswordField passwordField;

    public LoginView() {
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // Центральная панель
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(30));
        centerBox.getStyleClass().add("login-container");

        // Заголовок
        Label titleLabel = new Label("Магазин игрушек");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.getStyleClass().add("title-label");

        Label subtitleLabel = new Label("Вход в систему");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.getStyleClass().add("subtitle-label");

        // Поля ввода
        VBox fieldsBox = new VBox(15);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setMaxWidth(300);

        // Логин
        VBox loginBox = new VBox(5);
        Label loginLabel = new Label("Логин:");
        loginLabel.getStyleClass().add("field-label");
        loginField = new TextField();
        loginField.setPromptText("Введите логин");
        loginField.getStyleClass().add("text-field");
        loginBox.getChildren().addAll(loginLabel, loginField);

        // Пароль
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Пароль:");
        passwordLabel.getStyleClass().add("field-label");
        passwordField = new PasswordField();
        passwordField.setPromptText("Введите пароль");
        passwordField.getStyleClass().add("text-field");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        fieldsBox.getChildren().addAll(loginBox, passwordBox);

        // Кнопки
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Войти");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMinWidth(130);
        loginButton.setOnAction(e -> handleLogin());

        Button registerButton = new Button("Регистрация");
        registerButton.getStyleClass().add("secondary-button");
        registerButton.setMinWidth(130);
        registerButton.setOnAction(e -> handleRegistration());

        buttonBox.getChildren().addAll(loginButton, registerButton);

        // Обработка Enter
        passwordField.setOnAction(e -> handleLogin());

        centerBox.getChildren().addAll(titleLabel, subtitleLabel, fieldsBox, buttonBox);
        root.setCenter(centerBox);
    }

    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();

        // Валидация
        if (login.isEmpty() || password.isEmpty()) {
            DialogUtils.showError("Ошибка входа", "Пожалуйста, заполните все поля");
            return;
        }

        // Проверка администратора
        Administrator admin = new Administrator();
        if (admin.login(login, password)) {
            clearFields();
            MainApp.showAdminView();
            return;
        }

        // Проверка покупателя
        Customer customer = UserManager.getInstance().authenticateUser(login, password);
        if (customer != null) {
            clearFields();
            MainApp.showCustomerView(customer);
        } else {
            DialogUtils.showError("Ошибка входа", "Неверный логин или пароль");
        }
    }

    private void handleRegistration() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();

        // Валидация
        if (login.isEmpty() || password.isEmpty()) {
            DialogUtils.showError("Ошибка регистрации", "Пожалуйста, заполните все поля");
            return;
        }

        if (login.length() < 3) {
            DialogUtils.showError("Ошибка регистрации", "Логин должен содержать минимум 3 символа");
            return;
        }

        if (password.length() < 4) {
            DialogUtils.showError("Ошибка регистрации", "Пароль должен содержать минимум 4 символа");
            return;
        }

        // Проверка существования пользователя
        if (UserManager.getInstance().getUser(login) != null) {
            DialogUtils.showError("Ошибка регистрации", "Пользователь с таким логином уже существует");
            return;
        }

        // Регистрация
        Customer newCustomer = new Customer();
        newCustomer.register(login, password);

        DialogUtils.showInfo("Успех", "Регистрация успешно завершена!\nТеперь вы можете войти в систему.");
        clearFields();
    }

    private void clearFields() {
        loginField.clear();
        passwordField.clear();
    }

    public BorderPane getView() {
        return root;
    }
}
