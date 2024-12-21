
import java.io.Serializable;
import java.util.Map;

public class Order implements Serializable {
  String orderId;
  String customerName;
  Map<String, Integer> items;

  public Order(String orderId, String customerName, Map<String, Integer> items) {
    this.orderId = orderId;
    this.customerName = customerName;
    this.items = items;
  }
  @Override
  public String toString() {
    StringBuilder itemsString = new StringBuilder();
    for (Map.Entry<String, Integer> entry : items.entrySet()) {
      itemsString.append(String.format("Id: %s Quantity: %d, ", entry.getKey(), entry.getValue()));
    }
    return String.format("Order ID: %s, Customer Name: %s, Items: {%s}",
            orderId, customerName, itemsString);
  }

}