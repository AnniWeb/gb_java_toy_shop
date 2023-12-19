/**
 * Розыгрыш игрушки
 */
public class Raffle {
    private int toyId;
    private String title;
    private int quantity;
    private int rate;

    public Raffle(int toyId, String title, int quantity, int rate) {
        this.toyId = toyId;
        this.title = title;
        this.quantity = quantity;
        this.rate = rate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * частота выпадения игрушки (вес в % от 100)
     * @return
     */
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getToyId() {
        return toyId;
    }
}