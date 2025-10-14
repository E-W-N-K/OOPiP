// Cart.java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.io.Serializable;

public class Cart implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;

    private final ArrayList<Toys> mainCart;
    private List<Toys> displayedCart;

    private String filter_Size = null;
    private String filter_Type = null;
    private int sortOrder = 0;

    public Cart(){
        this.mainCart = new ArrayList<>();
        this.displayedCart = new ArrayList<>();
    }

    public void add_toy(Toys toy){
        this.mainCart.add(toy);
        resetDisplayedCart();
    }

    public void show_cart(){
        System.out.println("Вот ваша корзина: ");
        int i = 0;
        for (Toys toy : displayedCart) {
            System.out.println((i + 1) + ". Размер: " + toy.getSize() +
                    ", Тип: " + toy.getType() +
                    ", Цена: " + toy.getPrice() + " руб.");
            i++;
        }

        System.out.println("Стоимость корзины: ");
        ISumCart sum_Cart = arr -> {
            int s = 0;
            for (Toys toy : arr) {
                s += toy.getPrice();
            }
            return s;
        };

        int total = sum_Cart.sumCart(new ArrayList<>(displayedCart));
        System.out.println(total + " рублей");
    }

    public void delete_toy(int i){
        if(i < 1 || i > displayedCart.size()) {
            System.out.println("Неверный номер товара.");
            return;
        }
        Toys toy = displayedCart.get(i - 1);
        mainCart.remove(toy);
        resetDisplayedCart();
    }

    private void resetDisplayedCart() {
        displayedCart = mainCart.stream()
                .filter(toy -> filter_Size == null || toy.getSize().equals(filter_Size))
                .filter(toy -> filter_Type == null || toy.getType().equals(filter_Type))
                .collect(Collectors.toList());

        if (sortOrder == 1) {
            displayedCart.sort(Comparator.comparing(Toys::getPrice));
        } else if (sortOrder == 2) {
            displayedCart.sort(Comparator.comparing(Toys::getPrice).reversed());
        }
    }

    public void filter(String type_filter, String value){
        switch (type_filter){
            case "size": {
                filter_Size = value;
                break;
            }
            case "type": {
                filter_Type = value;
                break;
            }
        }
        resetDisplayedCart();
    }

    public void removeFilter(String filterType) {
        if(filterType.equals("size")) {
            filter_Size = null;
        }
        if(filterType.equals("type")) {
            filter_Type = null;
        }
        resetDisplayedCart();
    }

    @Override
    public void run() {
        sortOrder = 1;
        resetDisplayedCart();
    }

    public void sortDescending() {
        sortOrder = 2;
        resetDisplayedCart();
    }

    public void cancelSort() {
        sortOrder = 0;
        resetDisplayedCart();
    }

    public int getItemCount() {
        return mainCart.size();
    }

    public boolean isEmpty() {
        return mainCart.isEmpty();
    }
}