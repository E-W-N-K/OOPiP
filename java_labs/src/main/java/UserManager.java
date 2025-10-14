// UserManager.java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class UserManager {
    private static UserManager instance;
    private Map<String, Customer> users;
    private static final String USERS_FILE = "users.dat";

    private UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void saveUser(Customer user) {
        users.put(user.getLogin(), user);
        saveUsers();
    }

    public Customer getUser(String login) {
        return users.get(login);
    }

    public boolean deleteUser(String login) {
        Customer removed = users.remove(login);
        if (removed != null) {
            saveUsers();
            return true;
        }
        return false;
    }

    public Collection<Customer> getAllUsers() {
        return users.values();
    }

    public void saveUserCart(Customer user) {
        if (users.containsKey(user.getLogin())) {
            users.put(user.getLogin(), user);
            saveUsers();
        }
    }

    public Customer authenticateUser(String login, String password) {
        Customer user = users.get(login);
        if (user != null && user.login(login, password)) {
            return user;
        }
        return null;
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(USERS_FILE)))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения пользователей: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(USERS_FILE)))) {
            users = (Map<String, Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Файл не существует или поврежден - создаем новую карту
            users = new HashMap<>();
        }
    }
}

