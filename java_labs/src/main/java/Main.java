// Main.java
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean continueRunning = true;

        System.out.println("=== СИСТЕМА УПРАВЛЕНИЯ МАГАЗИНОМ ИГРУШЕК ===");

        do {
            System.out.println("\nВыберите роль:");
            System.out.println("1. Администратор");
            System.out.println("2. Покупатель (вход)");
            System.out.println("3. Покупатель (новый пользователь)");
            System.out.println("4. Выход из программы");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: {
                    handleAdministratorLogin(scanner);
                    break;
                }
                case 2: {
                    handleCustomerLogin(scanner);
                    break;
                }
                case 3: {
                    handleNewCustomer(scanner);
                    break;
                }
                case 4: {
                    continueRunning = false;
                    System.out.println("Спасибо за использование системы!");
                    break;
                }
                default: {
                    System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        } while (continueRunning);

        scanner.close();
    }

    private static void handleAdministratorLogin(Scanner scanner) {
        System.out.println("=== ВХОД АДМИНИСТРАТОРА ===");
        System.out.println("Введите логин:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        Administrator admin = new Administrator();
        if (admin.login(login, password)) {
            System.out.println("Добро пожаловать, администратор!");
            admin.handleAdminActions(scanner);
        } else {
            System.out.println("Неверные данные для входа администратора.");
        }
    }

    private static void handleCustomerLogin(Scanner scanner) {
        System.out.println("=== ВХОД ПОКУПАТЕЛЯ ===");
        System.out.println("Введите логин:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        Customer customer = UserManager.getInstance().authenticateUser(login, password);
        if (customer != null) {
            System.out.println("Добро пожаловать, " + customer.getLogin() + "!");
            System.out.println("Статус: " + customer.getState().getStateDescription());
            customer.handleCustomerActions(scanner);
        } else {
            System.out.println("Неверные данные для входа.");
        }
    }

    private static void handleNewCustomer(Scanner scanner) {
        System.out.println("=== НОВЫЙ ПОКУПАТЕЛЬ ===");
        Customer newCustomer = new Customer();
        newCustomer.handleCustomerActions(scanner);
    }
}