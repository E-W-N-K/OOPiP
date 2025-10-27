package org.lab5.lab5;// Administrator.java
import java.util.Collection;
import java.util.Scanner;

public class Administrator implements User {
    private String login;
    private String password;
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public Administrator() {
        this.login = ADMIN_LOGIN;
        this.password = ADMIN_PASSWORD;
    }

    @Override
    public void register(String login, String password) {
        // Администратор не может регистрироваться - аккаунт предопределён
        System.out.println("Администратор не может изменить свои данные.");
    }

    @Override
    public boolean login(String login, String password) {
        return ADMIN_LOGIN.equals(login) && ADMIN_PASSWORD.equals(password);
    }

    @Override
    public void showMenu() {
        System.out.println("=== ПАНЕЛЬ АДМИНИСТРАТОРА ===");
        System.out.println("Выберите действие:");
        System.out.println("1. Управление пользователями");
        System.out.println("2. Управление каталогом игрушек");
        System.out.println("3. Просмотр всех пользователей");
        System.out.println("0. Выход");
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public UserState getState() {
        return null; // У администратора нет состояний
    }

    @Override
    public void setState(UserState state) {
        // У администратора нет состояний
    }

    public void handleAdminActions(Scanner scanner) {
        int choice;
        do {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleUserManagement(scanner);
                    break;
                case 2:
                    handleCatalogManagement(scanner);
                    break;
                case 3:
                    showAllUsers();
                    break;
                case 0:
                    System.out.println("Выход из панели администратора.");
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        } while (choice != 0);
    }

    private void handleUserManagement(Scanner scanner) {
        int choice;
        do {
            System.out.println("=== УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ ===");
            System.out.println("1. Заблокировать пользователя");
            System.out.println("2. Разблокировать пользователя");
            System.out.println("3. Удалить пользователя");
            System.out.println("4. Просмотр состояния пользователя");
            System.out.println("0. Назад");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    blockUser(scanner);
                    break;
                case 2:
                    unblockUser(scanner);
                    break;
                case 3:
                    deleteUser(scanner);
                    break;
                case 4:
                    showUserStatus(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        } while (choice != 0);
    }

    private void blockUser(Scanner scanner) {
        System.out.println("Введите логин пользователя для блокировки:");
        String login = scanner.nextLine();

        Customer user = UserManager.getInstance().getUser(login);
        if (user != null && user.isRegistered()) {
            user.setState(UserStateType.BLOCKED);
            UserManager.getInstance().saveUser(user);
            System.out.println("Пользователь " + login + " заблокирован.");
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private void unblockUser(Scanner scanner) {
        System.out.println("Введите логин пользователя для разблокировки:");
        String login = scanner.nextLine();

        Customer user = UserManager.getInstance().getUser(login);
        if (user != null && user.isRegistered()) {
            user.setState(UserStateType.ACTIVE);
            UserManager.getInstance().saveUser(user);
            System.out.println("Пользователь " + login + " разблокирован.");
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private void deleteUser(Scanner scanner) {
        System.out.println("Введите логин пользователя для удаления:");
        String login = scanner.nextLine();

        if (UserManager.getInstance().deleteUser(login)) {
            System.out.println("Пользователь " + login + " удалён.");
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private void showUserStatus(Scanner scanner) {
        System.out.println("Введите логин пользователя:");
        String login = scanner.nextLine();

        Customer user = UserManager.getInstance().getUser(login);
        if (user != null) {
            System.out.println("Пользователь: " + user.getLogin());
            System.out.println("Состояние: " + user.getState().getStateDescription());
            System.out.println("Товаров в корзине: " + user.getCart().getItemCount());
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private void showAllUsers() {
        System.out.println("=== СПИСОК ВСЕХ ПОЛЬЗОВАТЕЛЕЙ ===");
        Collection<Customer> users = UserManager.getInstance().getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
        } else {
            for (Customer user : users) {
                System.out.println("Логин: " + user.getLogin() +
                        " | Состояние: " + user.getState().getStateDescription() +
                        " | Товаров в корзине: " + user.getCart().getItemCount());
            }
        }
    }

    private void handleCatalogManagement(Scanner scanner) {
        int choice;
        do {
            System.out.println("=== УПРАВЛЕНИЕ КАТАЛОГОМ ===");
            System.out.println("1. Просмотр каталога");
            System.out.println("2. Добавить тип игрушки");
            System.out.println("3. Изменить цену игрушки");
            System.out.println("4. Удалить тип игрушки");
            System.out.println("0. Назад");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ToysCatalog.getInstance().showCatalog();
                    break;
                case 2:
                    addToyType(scanner);
                    break;
                case 3:
                    modifyToyPrice(scanner);
                    break;
                case 4:
                    removeToyType(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        } while (choice != 0);
    }

    private void addToyType(Scanner scanner) {
        System.out.println("Введите тип новой игрушки:");
        String type = scanner.nextLine();
        System.out.println("Введите базовую цену:");
        int price = scanner.nextInt();
        scanner.nextLine();

        ToysCatalog.getInstance().addToyType(type, price);
        System.out.println("Тип игрушки '" + type + "' добавлен с базовой ценой " + price);
    }

    private void modifyToyPrice(Scanner scanner) {
        System.out.println("Введите тип игрушки для изменения цены:");
        String type = scanner.nextLine();
        System.out.println("Введите новую базовую цену:");
        int price = scanner.nextInt();
        scanner.nextLine();

        if (ToysCatalog.getInstance().modifyToyPrice(type, price)) {
            System.out.println("Цена для типа '" + type + "' изменена на " + price);
        } else {
            System.out.println("Тип игрушки не найден.");
        }
    }

    private void removeToyType(Scanner scanner) {
        System.out.println("Введите тип игрушки для удаления:");
        String type = scanner.nextLine();

        if (ToysCatalog.getInstance().removeToyType(type)) {
            System.out.println("Тип игрушки '" + type + "' удалён из каталога.");
        } else {
            System.out.println("Тип игрушки не найден.");
        }
    }
}

