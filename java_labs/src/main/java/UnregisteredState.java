import java.io.Serializable;

// UnregisteredState.java
public class UnregisteredState extends UserState implements Serializable {
    public UnregisteredState(Customer customer) {
        super(customer);
    }

    @Override
    public void showMenu() {
        System.out.println("Для продолжения работы необходимо зарегистрироваться.");
        System.out.println("Доступные действия:\n" +
                "1. Регистрация\n" +
                "0. Выход");
    }

    @Override
    public boolean canAccessShop() {
        return false;
    }

    @Override
    public String getStateDescription() {
        return "Незарегистрированный пользователь";
    }
}


