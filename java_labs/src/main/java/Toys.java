import java.util.ArrayList;
import java.util.Arrays;

public class Toys {
    // Для каждого объекта конкретный размер и тип
    private String size;
    private String type;
    private int price;

    // Конструктор с выбором конкретного размера и типа
    public Toys(String size, String type) {
        this.size = size;
        this.type = type;
        //образование цены
        switch (type) {
            case "car":
                this.price = 150;
                break;
            case "lego":
                this.price = 300;
                break;
            case "soft-toy":
                this.price = 200;
                break;
        }
        switch (size){
            case "medium":
                this.price *= 1.5;
                break;
            case "big":
                this.price *=2.5;
                break;
        }
    }

    public int getPrice() {
        return price;
    }

    public String getSize() {return size; }

    public String getType() {return type; }

}
