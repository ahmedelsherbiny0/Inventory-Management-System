import java.io.Serializable;

public class Sale implements Serializable {
  String itemId;
  String name;
  double price;
  int quantity;
  public Sale(String itemId, String name, double price, int quantity) {
    this.itemId = itemId;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return String.format("Item ID: %s, Name: %s, Quantity Sold: %d, Total Price: $%.2f",
            itemId, name, quantity, price);
  }
}
