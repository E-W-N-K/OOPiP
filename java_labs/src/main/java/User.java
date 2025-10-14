// User.java
public interface User {
    void register(String login, String password);

    boolean login(String login, String password);

    void showMenu();

    String getLogin();

    UserState getState();

    void setState(UserState state);
}
