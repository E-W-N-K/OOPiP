import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean c = true;
        do {
            System.out.println("Выберите роль: \n" +
                    "1. Директор \n" +
                    "2. Покупатель \n" +
                    "3. Выход из программы");
            int choise = scanner.nextInt();
            switch (choise) {
                case 1: {
                    // интерфейс директора
                    do {
                        System.out.println("Выберите действие:\n" +
                                "0. Выход");
                        choise = scanner.nextInt();
                    } while (choise != 0);
                    break;
                }
                case 2: {
                    // интерфейс покупателя
                    Cart cart = new Cart();
                    do {
                        System.out.println(
                                "Выберите действие:\n" +
                                        "1. Выбрать мягкую игрушку\n" +
                                        "2. Выбрать конструктор\n" +
                                        "3. Выбрать машинку\n" +
                                        "4. Посмотреть корзину\n" +
                                        "5. Фильтрация\n" +
                                        "6. Отменить фильтр\n" +
                                        "7. Удалить из корзины товар\n" +
                                        "8. Сортировка корзины\n" +
                                        "9. Отмена сортировки\n" +
                                        "0. Выход");
                        choise = scanner.nextInt();
                        scanner.nextLine();

                        switch (choise) {
                            case 1: {
                                System.out.println("Выберите размер игрушки: small, medium, big");
                                String size = scanner.nextLine();
                                Toys toy = new Toys(size, "soft-toy");
                                cart.add_toy(toy);
                                System.out.println("Мягкая игрушка добавлена в корзину.");
                                break;
                            }
                            case 2: {
                                System.out.println("Выберите размер игрушки: small, medium, big");
                                String size = scanner.nextLine();
                                Toys toy = new Toys(size, "lego");
                                cart.add_toy(toy);
                                System.out.println("Конструктор добавлен в корзину.");
                                break;
                            }
                            case 3: {
                                System.out.println("Выберите размер игрушки: small, medium, big");
                                String size = scanner.nextLine();
                                Toys toy = new Toys(size, "car");
                                cart.add_toy(toy);
                                System.out.println("Машинка добавлена в корзину.");
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
                                System.out.println("Введите номер игрушки:");
                                int ch = scanner.nextInt();
                                cart.delete_toy(ch);
                                System.out.println("Игрушка удалена из вашей корзины");
                                cart.show_cart();
                                scanner.nextLine();
                                break;
                            }
                            case 8: {
                                System.out.println("Как вы хотите отсортировать данные?");
                                System.out.println("1. По возрастанию цены\n" +
                                        "2. По убыванию цены");
                                int ch = scanner.nextInt();

                                switch (ch) {
                                    case 1: {
                                        // Создаем поток через Runnable для сортировки по возрастанию
                                        Thread runnuble_Thread = new Thread(cart);
                                        runnuble_Thread.start();
                                        try {
                                            runnuble_Thread.join();
                                        } catch (InterruptedException e) {
                                            System.out.println("Поток был прерван: " + e.getMessage());
                                        }
                                        System.out.println("Ваша корзина отсортирована по возрастанию");
                                        break;
                                    }
                                    case 2: {
                                        // Создаем поток через наследование Thread для сортировки по убыванию
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
                                break;
                            }
                            default: {
                                System.out.println("Неверный выбор. Попробуйте снова.");
                                break;
                            }
                        }
                    } while (choise != 0);
                    break;
                }
                case 3: {
                    c = false;
                    break;
                }
                default: {
                    System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        } while (c);
        scanner.close();
    }
}
