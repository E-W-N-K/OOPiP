package org.lab5.lab5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminView {

    private BorderPane root;
    private TableView<UserTableData> usersTable;
    private TableView<ToyTableData> catalogTable;

    public AdminView() {
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("root");

        // Верхняя панель
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Центральная панель с вкладками
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add("tab-pane");

        // Вкладка управления пользователями
        Tab usersTab = new Tab("Управление пользователями");
        usersTab.setContent(createUsersPanel());

        // Вкладка управления каталогом
        Tab catalogTab = new Tab("Управление каталогом");
        catalogTab.setContent(createCatalogPanel());

        tabPane.getTabs().addAll(usersTab, catalogTab);

        root.setCenter(tabPane);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");
        topBar.setSpacing(15);

        Label titleLabel = new Label("Панель администратора");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.getStyleClass().add("title-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Выход");
        logoutButton.getStyleClass().add("secondary-button");
        logoutButton.setOnAction(e -> handleLogout());

        topBar.getChildren().addAll(titleLabel, spacer, logoutButton);
        return topBar;
    }

    private VBox createUsersPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        // Таблица пользователей
        usersTable = new TableView<>();
        usersTable.getStyleClass().add("table-view");

        TableColumn<UserTableData, String> loginCol = new TableColumn<>("Логин");
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginCol.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.25));

        TableColumn<UserTableData, String> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.25));

        TableColumn<UserTableData, Integer> cartCol = new TableColumn<>("Товаров в корзине");
        cartCol.setCellValueFactory(new PropertyValueFactory<>("cartItems"));
        cartCol.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.24));

        usersTable.getColumns().addAll(loginCol, statusCol, cartCol);
        VBox.setVgrow(usersTable, Priority.ALWAYS);

        // Кнопки управления
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button refreshButton = new Button("Обновить");
        refreshButton.getStyleClass().add("primary-button");
        refreshButton.setOnAction(e -> loadUsers());

        Button blockButton = new Button("Заблокировать");
        blockButton.getStyleClass().add("warning-button");
        blockButton.setOnAction(e -> blockUser());

        Button unblockButton = new Button("Разблокировать");
        unblockButton.getStyleClass().add("success-button");
        unblockButton.setOnAction(e -> unblockUser());

        Button deleteButton = new Button("Удалить");
        deleteButton.getStyleClass().add("danger-button");
        deleteButton.setOnAction(e -> deleteUser());

        buttonBox.getChildren().addAll(refreshButton, blockButton, unblockButton, deleteButton);

        panel.getChildren().addAll(usersTable, buttonBox);

        // Загружаем пользователей
        loadUsers();

        return panel;
    }

    private VBox createCatalogPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        // Таблица каталога
        catalogTable = new TableView<>();
        catalogTable.getStyleClass().add("table-view");

        TableColumn<ToyTableData, String> typeCol = new TableColumn<>("Тип игрушки");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.prefWidthProperty().bind(catalogTable.widthProperty().multiply(0.49));

        TableColumn<ToyTableData, Integer> priceCol = new TableColumn<>("Базовая цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        priceCol.prefWidthProperty().bind(catalogTable.widthProperty().multiply(0.49));

        catalogTable.getColumns().addAll(typeCol, priceCol);
        VBox.setVgrow(catalogTable, Priority.ALWAYS);

        // Кнопки управления
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button refreshButton = new Button("Обновить");
        refreshButton.getStyleClass().add("primary-button");
        refreshButton.setOnAction(e -> loadCatalog());

        Button addButton = new Button("Добавить");
        addButton.getStyleClass().add("success-button");
        addButton.setOnAction(e -> addToyType());

        Button editButton = new Button("Изменить цену");
        editButton.getStyleClass().add("warning-button");
        editButton.setOnAction(e -> editToyPrice());

        Button deleteButton = new Button("Удалить");
        deleteButton.getStyleClass().add("danger-button");
        deleteButton.setOnAction(e -> deleteToyType());

        buttonBox.getChildren().addAll(refreshButton, addButton, editButton, deleteButton);

        panel.getChildren().addAll(catalogTable, buttonBox);

        // Загружаем каталог
        loadCatalog();

        return panel;
    }

    // ===== Методы управления пользователями =====

    private void loadUsers() {
        ObservableList<UserTableData> users = FXCollections.observableArrayList();
        for (Customer customer : UserManager.getInstance().getAllUsers()) {
            users.add(new UserTableData(
                    customer.getLogin(),
                    customer.getState().getStateDescription(),
                    customer.getCart().getItemCount()
            ));
        }
        usersTable.setItems(users);
    }

    private void blockUser() {
        UserTableData selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Предупреждение", "Выберите пользователя из списка");
            return;
        }

        Customer user = UserManager.getInstance().getUser(selected.getLogin());
        if (user != null) {
            user.setState(UserStateType.BLOCKED);
            UserManager.getInstance().saveUser(user);
            DialogUtils.showInfo("Успех", "Пользователь " + selected.getLogin() + " заблокирован");
            loadUsers();
        }
    }

    private void unblockUser() {
        UserTableData selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Предупреждение", "Выберите пользователя из списка");
            return;
        }

        Customer user = UserManager.getInstance().getUser(selected.getLogin());
        if (user != null) {
            user.setState(UserStateType.ACTIVE);
            UserManager.getInstance().saveUser(user);
            DialogUtils.showInfo("Успех", "Пользователь " + selected.getLogin() + " разблокирован");
            loadUsers();
        }
    }

    private void deleteUser() {
        UserTableData selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Предупреждение", "Выберите пользователя из списка");
            return;
        }

        boolean confirm = DialogUtils.showConfirm("Подтверждение",
                "Вы уверены, что хотите удалить пользователя " + selected.getLogin() + "?");

        if (confirm) {
            if (UserManager.getInstance().deleteUser(selected.getLogin())) {
                DialogUtils.showInfo("Успех", "Пользователь удалён");
                loadUsers();
            }
        }
    }

    // ===== Методы управления каталогом =====

    private void loadCatalog() {
        ObservableList<ToyTableData> catalog = FXCollections.observableArrayList();
        for (var entry : ToysCatalog.getInstance().getAllToyTypes().entrySet()) {
            catalog.add(new ToyTableData(entry.getKey(), entry.getValue()));
        }
        catalogTable.setItems(catalog);
    }

    private void addToyType() {
        Dialog<ToyTypeInput> dialog = new Dialog<>();
        dialog.setTitle("Добавить тип игрушки");
        dialog.setHeaderText("Введите данные нового типа игрушки");
        DialogUtils.setDialogIcon(dialog);

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField typeField = new TextField();
        typeField.setPromptText("Название типа");
        TextField priceField = new TextField();
        priceField.setPromptText("Базовая цена");

        grid.add(new Label("Тип:"), 0, 0);
        grid.add(typeField, 1, 0);
        grid.add(new Label("Цена:"), 0, 1);
        grid.add(priceField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String type = typeField.getText().trim();
                    int price = Integer.parseInt(priceField.getText().trim());

                    if (type.isEmpty() || price <= 0) {
                        return null;
                    }

                    return new ToyTypeInput(type, price);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            ToysCatalog.getInstance().addToyType(result.type, result.price);
            DialogUtils.showInfo("Успех", "Тип игрушки '" + result.type + "' добавлен");
            loadCatalog();
        });
    }

    private void editToyPrice() {
        ToyTableData selected = catalogTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Предупреждение", "Выберите тип игрушки из списка");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getBasePrice()));
        dialog.setTitle("Изменить цену");
        dialog.setHeaderText("Изменение цены для типа: " + selected.getType());
        dialog.setContentText("Новая цена:");
        DialogUtils.setDialogIcon(dialog);

        dialog.showAndWait().ifPresent(priceStr -> {
            try {
                int newPrice = Integer.parseInt(priceStr.trim());
                if (newPrice <= 0) {
                    DialogUtils.showError("Ошибка", "Цена должна быть больше нуля");
                    return;
                }

                ToysCatalog.getInstance().modifyToyPrice(selected.getType(), newPrice);
                DialogUtils.showInfo("Успех", "Цена изменена");
                loadCatalog();
            } catch (NumberFormatException e) {
                DialogUtils.showError("Ошибка", "Введите корректное числовое значение");
            }
        });
    }

    private void deleteToyType() {
        ToyTableData selected = catalogTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Предупреждение", "Выберите тип игрушки из списка");
            return;
        }

        boolean confirm = DialogUtils.showConfirm("Подтверждение",
                "Вы уверены, что хотите удалить тип игрушки '" + selected.getType() + "'?");

        if (confirm) {
            ToysCatalog.getInstance().removeToyType(selected.getType());
            DialogUtils.showInfo("Успех", "Тип игрушки удалён");
            loadCatalog();
        }
    }

    private void handleLogout() {
        boolean confirm = DialogUtils.showConfirm("Выход", "Вы уверены, что хотите выйти?");
        if (confirm) {
            MainApp.showLoginView();
        }
    }

    public BorderPane getView() {
        return root;
    }

    // ===== Вспомогательные классы =====

    public static class UserTableData {
        private String login;
        private String status;
        private int cartItems;

        public UserTableData(String login, String status, int cartItems) {
            this.login = login;
            this.status = status;
            this.cartItems = cartItems;
        }

        public String getLogin() { return login; }
        public String getStatus() { return status; }
        public int getCartItems() { return cartItems; }
    }

    public static class ToyTableData {
        private String type;
        private int basePrice;

        public ToyTableData(String type, int basePrice) {
            this.type = type;
            this.basePrice = basePrice;
        }

        public String getType() { return type; }
        public int getBasePrice() { return basePrice; }
    }

    private static class ToyTypeInput {
        String type;
        int price;

        ToyTypeInput(String type, int price) {
            this.type = type;
            this.price = price;
        }
    }
}
