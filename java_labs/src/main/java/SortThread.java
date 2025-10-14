// SortThread.java
public class SortThread extends Thread {
    private final Cart cart;

    public SortThread(Cart cart) {
        this.cart = cart;
    }

    @Override
    public void run() {
        synchronized (cart) {
            cart.sortDescending();
        }
    }
}