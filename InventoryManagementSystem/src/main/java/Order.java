
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

}