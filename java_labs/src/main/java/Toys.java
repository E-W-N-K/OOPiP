// Toys.java
import java.io.Serializable;

public class Toys implements Serializable {
    private static final long serialVersionUID = 1L;

    private String size;
    private String type;
    private int price;

    public Toys(String size, String type, int basePrice) {
        this.size = size;
        this.type = type;
        this.price = calculatePrice(basePrice, size);
    }

    // Старый конструктор для обратной совместимости
    public Toys(String size, String type) {
        this.size = size;
        this.type = type;
        this.price = calculateOldPrice(type, size);
    }

    private int calculatePrice(int basePrice, String size) {
        switch (size) {
            case "medium":
                return (int) (basePrice * 1.5);
            case "big":
                return (int) (basePrice * 2.5);
            default: // "small"
                return basePrice;
        }
    }

    private int calculateOldPrice(String type, String size) {
        int basePrice;
        switch (type) {
            case "car":
                basePrice = 150;
                break;
            case "lego":
                basePrice = 300;
                break;
            case "soft-toy":
                basePrice = 200;
                break;
            default:
                basePrice = 100;
        }
        return calculatePrice(basePrice, size);
    }

    public int getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Toys{size='" + size + "', type='" + type + "', price=" + price + "}";
    }
}










