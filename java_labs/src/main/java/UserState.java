import java.io.Serializable;

// UserState.java
public abstract class UserState implements Serializable {
    protected Customer customer;

    public UserState(Customer customer) {
        this.customer = customer;
    }

    public abstract void showMenu();

    public abstract boolean canAccessShop();

    public abstract String getStateDescription();
}
