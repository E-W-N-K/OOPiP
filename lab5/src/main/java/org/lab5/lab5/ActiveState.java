package org.lab5.lab5;

import java.io.Serializable;

// ActiveState.java
public class ActiveState extends UserState implements Serializable {
    public ActiveState(Customer customer) {
        super(customer);
    }

    @Override
    public void showMenu() {
        System.out.println("Выберите действие:\n" +
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
    }

    @Override
    public boolean canAccessShop() {
        return true;
    }

    @Override
    public String getStateDescription() {
        return "Активный пользователь";
    }
}
