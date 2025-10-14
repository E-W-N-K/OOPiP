import java.io.Serializable;

// BlockedState.java
public class BlockedState extends UserState implements Serializable {
    public BlockedState(Customer customer) {
        super(customer);
    }

    @Override
    public void showMenu() {
        System.out.println("Ваш аккаунт заблокирован администратором.");
        System.out.println("Доступные действия:\n" +
                "0. Выход");
    }

    @Override
    public boolean canAccessShop() {
        return false;
    }

    @Override
    public String getStateDescription() {
        return "Заблокированный пользователь";
    }
}
