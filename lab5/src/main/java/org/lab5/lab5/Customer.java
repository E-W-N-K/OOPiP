package org.lab5.lab5;// Customer.java
import java.util.Scanner;
import java.io.Serializable;

public class Customer implements User, Serializable {
    private static final long serialVersionUID = 1L;

    private String login;
    private String password;
    private boolean isRegistered;
    private UserState currentState;
    private Cart cart;

    public Customer() {
        this.cart = new Cart();
        this.currentState = new UnregisteredState(this);
        this.isRegistered = false;
    }

    public Customer(String login, String password, UserStateType stateType) {
        this.login = login;
        this.password = password;
        this.isRegistered = true;
        this.cart = new Cart();
        setState(stateType);
    }

    @Override
    public void register(String login, String password) {
        this.login = login;
        this.password = password;
        this.isRegistered = true;
        this.currentState = new ActiveState(this);
        UserManager.getInstance().saveUser(this);
        System.out.println("Регистрация успешно завершена!");
    }

    @Override
    public boolean login(String login, String password) {
        return this.login != null && this.login.equals(login) && this.password.equals(password);
    }

    @Override
    public void showMenu() {
        currentState.showMenu();
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public UserState getState() {
        return currentState;
    }

    @Override
    public void setState(UserState state) {
        this.currentState = state;
    }

    public void setState(UserStateType stateType) {
        switch (stateType) {
            case ACTIVE:
                this.currentState = new ActiveState(this);
                break;
            case BLOCKED:
                this.currentState = new BlockedState(this);
                break;
            case UNREGISTERED:
                this.currentState = new UnregisteredState(this);
                break;
        }
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public String getPassword() {
        return password;
    }

    // Обработка действий покупателя
    public void handleCustomerActions(Scanner scanner) {
        if (!currentState.canAccessShop()) {
            if (currentState instanceof UnregisteredState) {
                handleUnregisteredActions(scanner);
            } else {
                // Заблокированный пользователь
                int choice;
                do {
                    showMenu();
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice != 0) {
                        System.out.println("Доступен только выход из системы.");
                    }
                } while (choice != 0);
            }
            return;
        }

        handleShopActions(scanner);
    }

    private void handleUnregisteredActions(Scanner scanner) {
        int choice;
        do {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Введите логин:");
                    String login = scanner.nextLine();

                    // Проверяем, не существует ли уже такой пользователь
                    if (UserManager.getInstance().getUser(login) != null) {
                        System.out.println("Пользователь с таким логином уже существует!");
                        break;
                    }

                    System.out.println("Введите пароль:");
                    String password = scanner.nextLine();
                    register(login, password);
                    break;
                case 0:
                    System.out.println("Выход.");
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        } while (choice != 0 && currentState instanceof UnregisteredState);
    }

    private void handleShopActions(Scanner scanner) {
        int choice;
        do {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: {
                    System.out.println("Выберите размер игрушки: small, medium, big");
                    String size = scanner.nextLine();
                    Toys toy = ToysCatalog.getInstance().createToy(size, "soft-toy");
                    if (toy != null) {
                        cart.add_toy(toy);
                        System.out.println("Мягкая игрушка добавлена в корзину.");
                    }
                    break;
                }
                case 2: {
                    System.out.println("Выберите размер игрушки: small, medium, big");
                    String size = scanner.nextLine();
                    Toys toy = ToysCatalog.getInstance().createToy(size, "lego");
                    if (toy != null) {
                        cart.add_toy(toy);
                        System.out.println("Конструктор добавлен в корзину.");
                    }
                    break;
                }
                case 3: {
                    System.out.println("Выберите размер игрушки: small, medium, big");
                    String size = scanner.nextLine();
                    Toys toy = ToysCatalog.getInstance().createToy(size, "car");
                    if (toy != null) {
                        cart.add_toy(toy);
                        System.out.println("Машинка добавлена в корзину.");
                    }
                    break;
                }
                case 4: {
                    cart.show_cart();
                    break;
                }
                case 5: { // фильтрация
                    System.out.println("Выберите способ фильтрации (size, type):");
                    String filter = scanner.nextLine();
                    if (filter.equals("size")) {
                        System.out.println("Какой размер игрушек вы хотите? (small, medium, big)");
                        String value = scanner.nextLine();
                        cart.filter("size", value);
                    } else if (filter.equals("type")) {
                        System.out.println("Какой тип игрушек вы хотите? (car, lego, soft-toy)");
                        String value = scanner.nextLine();
                        cart.filter("type", value);
                    } else {
                        System.out.println("Неверный тип фильтра.");
                    }
                    System.out.println("Ваша корзина отфильтрована");
                    break;
                }
                case 6: { // отмена фильтра
                    System.out.println("Какой фильтр хотите отменить? (size, type)");
                    String filterToRemove = scanner.nextLine();
                    cart.removeFilter(filterToRemove);
                    System.out.println("Фильтр отменён");
                    break;
                }
                case 7: {
                    System.out.println("Выберите игрушку, которую хотите удалить:");
                    cart.show_cart();
                    if (!cart.isEmpty()) {
                        System.out.println("Введите номер игрушки:");
                        int ch = scanner.nextInt();
                        cart.delete_toy(ch);
                        System.out.println("Игрушка удалена из вашей корзины");
                        cart.show_cart();
                    }
                    scanner.nextLine();
                    break;
                }
                case 8: {
                    if (cart.isEmpty()) {
                        System.out.println("Корзина пуста. Нечего сортировать.");
                        break;
                    }

                    System.out.println("Как вы хотите отсортировать данные?");
                    System.out.println("1. По возрастанию цены\n" +
                            "2. По убыванию цены");
                    int ch = scanner.nextInt();

                    switch (ch) {
                        case 1: {
                            Thread runnableThread = new Thread(cart);
                            runnableThread.start();
                            try {
                                runnableThread.join();
                            } catch (InterruptedException e) {
                                System.out.println("Поток был прерван: " + e.getMessage());
                            }
                            System.out.println("Ваша корзина отсортирована по возрастанию");
                            break;
                        }
                        case 2: {
                            SortThread thread = new SortThread(cart);
                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                System.out.println("Поток был прерван: " + e.getMessage());
                            }
                            System.out.println("Ваша корзина отсортирована по убыванию");
                            break;
                        }
                        default: {
                            System.out.println("Неверный выбор сортировки.");
                        }
                    }
                    scanner.nextLine();
                    break;
                }
                case 9: {
                    cart.cancelSort();
                    System.out.println("Сортировка отменена");
                    break;
                }
                case 0: {
                    System.out.println("Выход из меню покупателя.");
                    UserManager.getInstance().saveUserCart(this);
                    break;
                }
                default: {
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    break;
                }
            }
        } while (choice != 0);
    }
}

