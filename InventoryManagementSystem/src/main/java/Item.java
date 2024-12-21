import java.io.Serializable;

public class Item implements Serializable {
  String itemId;
  String name;
  double price;
  int quantity;

  public Item(String itemId, String name, double price, int quantity) {
    this.itemId = itemId;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return String.format("%s | %s | %.2f | %d", itemId, name, price, quantity);
  }
}