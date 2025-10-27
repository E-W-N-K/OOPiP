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
import java.util.ArrayList;
import java.util.List;

public class CustomerView {

    private BorderPane root;
    private Customer customer;
    private TableView<CartItemData> cartTable;
    private Label totalLabel;

    // Поля фильтрации
    private ComboBox<String> sizeFilterCombo;
    private ComboBox<String> typeFilterCombo;

    public CustomerView(Customer customer) {
        this.customer = customer;

        // Проверка состояния пользователя
        if (customer.getState() instanceof BlockedState) {
            DialogUtils.showError("Доступ запрещён",
                    "Ваш аккаунт заблокирован администратором.\nОбратитесь к администратору для разблокировки.");
            MainApp.showLoginView();
            return;
        }

        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("root");

        // Верхняя панель
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Левая панель - добавление товаров
        VBox leftPanel = createLeftPanel();
        root.setLeft(leftPanel);

        // Центральная панель - корзина
        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");
        topBar.setSpacing(15);

        Label titleLabel = new Label("Магазин игрушек");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.getStyleClass().add("title-label");

        Label userLabel = new Label("Пользователь: " + customer.getLogin());
        userLabel.setFont(Font.font("System", 14));
        userLabel.getStyleClass().add("user-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Выход");
        logoutButton.getStyleClass().add("secondary-button");
        logoutButton.setOnAction(e -> handleLogout());

        topBar.getChildren().addAll(titleLabel, userLabel, spacer, logoutButton);
        return topBar;
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(280);
        leftPanel.getStyleClass().add("left-panel");

        Label addLabel = new Label("Добавить товар");
        addLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        addLabel.getStyleClass().add("section-title");

        // Выбор размера
        VBox sizeBox = new VBox(8);
        Label sizeLabel = new Label("Размер:");
        sizeLabel.getStyleClass().add("field-label");

        ComboBox<String> sizeCombo = new ComboBox<>();
        sizeCombo.getItems().addAll("small", "medium", "big");
        sizeCombo.setPromptText("Выберите размер");
        sizeCombo.getStyleClass().add("combo-box");
        sizeCombo.setMaxWidth(Double.MAX_VALUE);

        sizeBox.getChildren().addAll(sizeLabel, sizeCombo);

        // Кнопки добавления
        VBox buttonsBox = new VBox(10);

        Button addSoftToyButton = new Button("Мягкая игрушка");
        addSoftToyButton.setMaxWidth(Double.MAX_VALUE);
        addSoftToyButton.getStyleClass().add("add-button");
        addSoftToyButton.setOnAction(e -> addToCart(sizeCombo.getValue(), "soft-toy"));

        Button addLegoButton = new Button("Конструктор");
        addLegoButton.setMaxWidth(Double.MAX_VALUE);
        addLegoButton.getStyleClass().add("add-button");
        addLegoButton.setOnAction(e -> addToCart(sizeCombo.getValue(), "lego"));

        Button addCarButton = new Button("Машинка");
        addCarButton.setMaxWidth(Double.MAX_VALUE);
        addCarButton.getStyleClass().add("add-button");
        addCarButton.setOnAction(e -> addToCart(sizeCombo.getValue(), "car"));

        buttonsBox.getChildren().addAll(addSoftToyButton, addLegoButton, addCarButton);

        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        leftPanel.getChildren().addAll(addLabel, sizeBox, buttonsBox, separator);

        VBox.setVgrow(separator, Priority.ALWAYS);

        return leftPanel;
    }

    private VBox createCenterPanel() {
        VBox centerPanel = new VBox(15);
        centerPanel.setPadding(new Insets(20));

        // Заголовок
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label cartLabel = new Label("Корзина");
        cartLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        cartLabel.getStyleClass().add("section-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        totalLabel = new Label("Итого: 0 руб.");
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        totalLabel.getStyleClass().add("total-label");

        titleBox.getChildren().addAll(cartLabel, spacer, totalLabel);

        // Панель фильтров и сортировки
        HBox filtersBox = createFiltersBox();

        // Таблица корзины
        cartTable = new TableView<>();
        cartTable.getStyleClass().add("table-view");
        cartTable.setPlaceholder(new Label("Корзина пуста"));

        TableColumn<CartItemData, Integer> numCol = new TableColumn<>("№");
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        numCol.prefWidthProperty().bind(cartTable.widthProperty().multiply(0.08));

        TableColumn<CartItemData, String> sizeCol = new TableColumn<>("Размер");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.prefWidthProperty().bind(cartTable.widthProperty().multiply(0.20));

        TableColumn<CartItemData, String> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.prefWidthProperty().bind(cartTable.widthProperty().multiply(0.35));

        TableColumn<CartItemData, Integer> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.prefWidthProperty().bind(cartTable.widthProperty().multiply(0.20));

        TableColumn<CartItemData, Void> actionCol = new TableColumn<>("Действие");
        actionCol.prefWidthProperty().bind(cartTable.widthProperty().multiply(0.15));
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Удалить");

            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    CartItemData item = getTableView().getItems().get(getIndex());
                    deleteFromCart(item.getNumber());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        cartTable.getColumns().addAll(numCol, sizeCol, typeCol, priceCol, actionCol);
        VBox.setVgrow(cartTable, Priority.ALWAYS);

        centerPanel.getChildren().addAll(titleBox, filtersBox, cartTable);

        // Загружаем корзину
        updateCartView();

        return centerPanel;
    }

    private HBox createFiltersBox() {
        HBox filtersBox = new HBox(15);
        filtersBox.setPadding(new Insets(10));
        filtersBox.setAlignment(Pos.CENTER_LEFT);
        filtersBox.getStyleClass().add("filters-box");

        // Группа фильтров
        VBox filterGroup = new VBox(8);
        Label filterLabel = new Label("Фильтры:");
        filterLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        HBox filterControls = new HBox(10);

        sizeFilterCombo = new ComboBox<>();
        sizeFilterCombo.getItems().addAll("Все размеры", "small", "medium", "big");
        sizeFilterCombo.setValue("Все размеры");
        sizeFilterCombo.setPromptText("Размер");
        sizeFilterCombo.setOnAction(e -> applyFilters());

        typeFilterCombo = new ComboBox<>();
        typeFilterCombo.getItems().addAll("Все типы", "car", "lego", "soft-toy");
        typeFilterCombo.setValue("Все типы");
        typeFilterCombo.setPromptText("Тип");
        typeFilterCombo.setOnAction(e -> applyFilters());

        Button resetFiltersButton = new Button("Сбросить фильтры");
        resetFiltersButton.getStyleClass().add("secondary-button");
        resetFiltersButton.setOnAction(e -> resetFilters());

        filterControls.getChildren().addAll(sizeFilterCombo, typeFilterCombo, resetFiltersButton);
        filterGroup.getChildren().addAll(filterLabel, filterControls);

        Separator separator1 = new Separator();
        separator1.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Группа сортировки
        VBox sortGroup = new VBox(8);
        Label sortLabel = new Label("Сортировка:");
        sortLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        HBox sortControls = new HBox(10);

        Button sortAscButton = new Button("По возрастанию цены");
        sortAscButton.getStyleClass().add("sort-button");
        sortAscButton.setOnAction(e -> sortAscending());

        Button sortDescButton = new Button("По убыванию цены");
        sortDescButton.getStyleClass().add("sort-button");
        sortDescButton.setOnAction(e -> sortDescending());

        Button cancelSortButton = new Button("Отменить сортировку");
        cancelSortButton.getStyleClass().add("secondary-button");
        cancelSortButton.setOnAction(e -> cancelSort());

        sortControls.getChildren().addAll(sortAscButton, sortDescButton, cancelSortButton);
        sortGroup.getChildren().addAll(sortLabel, sortControls);

        filtersBox.getChildren().addAll(filterGroup, separator1, sortGroup);

        return filtersBox;
    }

    // ===== Методы работы с корзиной =====

    private void addToCart(String size, String type) {
        if (size == null || size.isEmpty()) {
            DialogUtils.showWarning("Предупреждение", "Выберите размер игрушки");
            return;
        }

        Toys toy = ToysCatalog.getInstance().createToy(size, type);
        if (toy != null) {
            customer.getCart().add_toy(toy);
            updateCartView();

            String typeName = switch (type) {
                case "soft-toy" -> "Мягкая игрушка";
                case "lego" -> "Конструктор";
                case "car" -> "Машинка";
                default -> "Игрушка";
            };

            DialogUtils.showInfo("Успех", typeName + " добавлена в корзину");
        }
    }

    private void deleteFromCart(int itemNumber) {
        boolean confirm = DialogUtils.showConfirm("Подтверждение",
                "Удалить товар №" + itemNumber + " из корзины?");

        if (confirm) {
            customer.getCart().delete_toy(itemNumber);
            updateCartView();
        }
    }

    private void applyFilters() {
        String sizeFilter = sizeFilterCombo.getValue();
        String typeFilter = typeFilterCombo.getValue();

        if (!sizeFilter.equals("Все размеры")) {
            customer.getCart().filter("size", sizeFilter);
        }

        if (!typeFilter.equals("Все типы")) {
            customer.getCart().filter("type", typeFilter);
        }

        updateCartView();
    }

    private void resetFilters() {
        sizeFilterCombo.setValue("Все размеры");
        typeFilterCombo.setValue("Все типы");
        customer.getCart().removeFilter("size");
        customer.getCart().removeFilter("type");
        updateCartView();
    }

    private void sortAscending() {
        Thread thread = new Thread(customer.getCart());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateCartView();
        DialogUtils.showInfo("Сортировка", "Корзина отсортирована по возрастанию цены");
    }

    private void sortDescending() {
        SortThread thread = new SortThread(customer.getCart());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateCartView();
        DialogUtils.showInfo("Сортировка", "Корзина отсортирована по убыванию цены");
    }

    private void cancelSort() {
        customer.getCart().cancelSort();
        updateCartView();
        DialogUtils.showInfo("Сортировка", "Сортировка отменена");
    }

    private void updateCartView() {
        // Получаем товары из корзины через вспомогательный метод
        List<Toys> items = getDisplayedItems();

        ObservableList<CartItemData> data = FXCollections.observableArrayList();
        for (int i = 0; i < items.size(); i++) {
            Toys toy = items.get(i);
            String typeDisplay = switch (toy.getType()) {
                case "soft-toy" -> "Мягкая игрушка";
                case "lego" -> "Конструктор";
                case "car" -> "Машинка";
                default -> toy.getType();
            };

            String sizeDisplay = switch (toy.getSize()) {
                case "small" -> "Маленький";
                case "medium" -> "Средний";
                case "big" -> "Большой";
                default -> toy.getSize();
            };

            data.add(new CartItemData(i + 1, sizeDisplay, typeDisplay, toy.getPrice()));
        }

        cartTable.setItems(data);

        // Обновляем итоговую сумму
        int total = items.stream().mapToInt(Toys::getPrice).sum();
        totalLabel.setText("Итого: " + total + " руб.");
    }

    // Вспомогательный метод для получения отображаемых товаров
    private List<Toys> getDisplayedItems() {
        // Создаем временную корзину для получения отфильтрованных данных
        Cart tempCart = customer.getCart();
        List<Toys> items = new ArrayList<>();

        // Используем рефлексию или добавим метод getDisplayedItems в Cart
        // Для простоты создадим новый список и применим ту же логику
        try {
            java.lang.reflect.Field field = Cart.class.getDeclaredField("displayedCart");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Toys> displayed = (List<Toys>) field.get(tempCart);
            items.addAll(displayed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    private void handleLogout() {
        boolean confirm = DialogUtils.showConfirm("Выход", "Вы уверены, что хотите выйти?");
        if (confirm) {
            UserManager.getInstance().saveUserCart(customer);
            MainApp.showLoginView();
        }
    }

    public BorderPane getView() {
        return root;
    }

    // ===== Вспомогательный класс =====

    public static class CartItemData {
        private int number;
        private String size;
        private String type;
        private int price;

        public CartItemData(int number, String size, String type, int price) {
            this.number = number;
            this.size = size;
            this.type = type;
            this.price = price;
        }

        public int getNumber() { return number; }
        public String getSize() { return size; }
        public String getType() { return type; }
        public int getPrice() { return price; }
    }
}
