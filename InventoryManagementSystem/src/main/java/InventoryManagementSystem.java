
import java.util.*;
import java.io.*;

public class InventoryManagementSystem {

  private static final String INVENTORY_FILE = "inventory.dat";
  private static final String ORDERS_FILE = "orders.dat";
  private static final String SALES_FILE = "sales.dat";
  private static List<Sale> salesHistory = new ArrayList<>();
  private static Map<String, Item> inventory = new HashMap<>();
  private static Queue<Order> orderQueue = new LinkedList<>();

  public static void main(String[] args) {
    loadInventory();
    loadOrders();
    loadSales();
    Scanner scanner = new Scanner(System.in);
    while (true) {
      showMainMenu();
      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1 -> manageInventory(scanner);
        case 2 -> processOrders(scanner);
        case 3 -> generateReports(scanner);
        case 4 -> {
          saveInventory();
          saveOrders();
          saveSales();
          System.out.println("Exiting... Goodbye!");
          System.exit(0);
        }
        default -> System.out.println("Invalid choice! Try again.");
      }
    }
  }

  private static void showMainMenu() {
    System.out.println("\n===== Inventory Management System =====");
    System.out.println("1. Inventory Management");
    System.out.println("2. Order Processing");
    System.out.println("3. Generate Reports");
    System.out.println("4. Exit");
    System.out.println("=======================================");
    System.out.print("Enter your choice: ");
  }

  // Inventory Management
  private static void manageInventory(Scanner scanner) {
    while (true) {
      System.out.println("\n===== Inventory Management =====");
      System.out.println("1. Add Item");
      System.out.println("2. Update Item");
      System.out.println("3. Remove Item");
      System.out.println("4. View All Items");
      System.out.println("5. Search Item");
      System.out.println("6. Back to Main Menu");
      System.out.print("Enter your choice: ");
      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1 -> addItem(scanner);
        case 2 -> updateItem(scanner);
        case 3 -> removeItem(scanner);
        case 4 -> viewAllItems();
        case 5 -> searchItem(scanner);
        case 6 -> {
          return;
        }
        default -> System.out.println("Invalid choice! Try again.");
      }
    }
  }

  private static void addItem(Scanner scanner) {
    System.out.print("Enter Item ID: ");
    String itemId = scanner.nextLine();
    if (inventory.containsKey(itemId)) {
      System.out.println("Item ID already exists!");
      return;
    }
    System.out.print("Enter Item Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter Item Price: ");
    double price = scanner.nextDouble();
    System.out.print("Enter Item Quantity: ");
    int quantity = scanner.nextInt();
    scanner.nextLine();

    inventory.put(itemId, new Item(itemId, name, price, quantity));
    System.out.println("Item added successfully!");
  }

  private static void updateItem(Scanner scanner) {
    System.out.print("Enter Item ID to update: ");
    String itemId = scanner.nextLine();
    Item item = inventory.get(itemId);
    if (item == null) {
      System.out.println("Item not found!");
      return;
    }
    System.out.print("Enter field to update (name/price/quantity): ");
    String field = scanner.nextLine().toLowerCase();
    switch (field) {
      case "name" -> {
        System.out.print("Enter new name: ");
        item.name = scanner.nextLine();
      }
      case "price" -> {
        System.out.print("Enter new price: ");
        item.price = scanner.nextDouble();
        scanner.nextLine();
      }
      case "quantity" -> {
        System.out.print("Enter new quantity: ");
        item.quantity = scanner.nextInt();
        scanner.nextLine();
      }
      default -> {
        System.out.println("Invalid field!");
        return;
      }
    }
    System.out.println("Item updated successfully!");
  }

  private static void removeItem(Scanner scanner) {
    System.out.print("Enter Item ID to remove: ");
    String itemId = scanner.nextLine();
    if (inventory.remove(itemId) != null) {
      System.out.println("Item removed successfully!");
    } else {
      System.out.println("Item not found!");
    }
  }

  private static void viewAllItems() {
    System.out.println("\nCurrent Inventory:");
    System.out.println("ID | Name | Price | Quantity");
    System.out.println("--------------------------------");
    for (Item item : inventory.values()) {
      System.out.println(item);
    }
  }

  private static void searchItem(Scanner scanner) {
    System.out.print("Enter Item ID or Name to search: ");
    String query = scanner.nextLine().toLowerCase();
    for (Item item : inventory.values()) {
      if (item.itemId.equalsIgnoreCase(query) || item.name.toLowerCase().contains(query)) {
        System.out.println(item);
        return;
      }
    }
    System.out.println("Item not found!");
  }

  // Order Processing
  private static void processOrders(Scanner scanner) {
    while (true) {
      System.out.println("\n===== Order Processing =====");
      System.out.println("1. Create Order");
      System.out.println("2. Process Next Order");
      System.out.println("3. View Pending Orders");
      System.out.println("4. Cancel Order");
      System.out.println("5. Back to Main Menu");
      System.out.print("Enter your choice: ");
      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1 -> createOrder(scanner);
        case 2 -> processNextOrder();
        case 3 -> viewPendingOrders();
        case 4 -> cancelOrder(scanner);
        case 5 -> {
          return;
        }
        default -> System.out.println("Invalid choice! Try again.");
      }
    }
  }

  private static void createOrder(Scanner scanner) {
    System.out.print("Enter Order ID: ");
    String orderId = scanner.nextLine();
    System.out.print("Enter Customer Name: ");
    String customerName = scanner.nextLine();
    Map<String, Integer> items = new HashMap<>();

    while (true) {
      System.out.print("Enter Item ID to add (or 'done' to finish): ");
      String itemId = scanner.nextLine();
      if (itemId.equalsIgnoreCase("done")) {
        break;
      }
      Item item = inventory.get(itemId);
      if (item == null) {
        System.out.println("Item not found!");
        continue;
      }
      System.out.print("Enter quantity: ");
      int quantity = scanner.nextInt();
      scanner.nextLine();
      if (quantity > item.quantity) {
        System.out.println("Not enough stock!");
        continue;
      }
      items.put(itemId, quantity);
    }

    orderQueue.add(new Order(orderId, customerName, items));
    System.out.println("Order created successfully!");
  }

  private static void processNextOrder() {
    Order order = orderQueue.poll();
    if (order == null) {
      System.out.println("No pending orders!");
      return;
    }
    for (Map.Entry<String, Integer> entry : order.items.entrySet()) {
      Item item = inventory.get(entry.getKey());
      if (item != null) {
        item.quantity -= entry.getValue();
        salesHistory.add(new Sale(item.itemId, item.name, item.price * entry.getValue(), entry.getValue()));
      }
    }
    System.out.println("Processed: " + order);
  }

  private static void viewPendingOrders() {
    System.out.println("\nPending Orders:");
    for (Order order : orderQueue) {
      System.out.println(order);
    }
  }

  private static void cancelOrder(Scanner scanner) {
    System.out.print("Enter Order ID to cancel: ");
    String orderId = scanner.nextLine();
    for (Order order : orderQueue) {
      if (order.orderId.equals(orderId)) {
        orderQueue.remove(order);
        System.out.println("Order canceled successfully!");
        return;
      }
    }
    System.out.println("Order not found!");
  }

  // Report Generation
  private static void generateReports(Scanner scanner) {
    while (true) {
      System.out.println("\n===== Reports =====");
      System.out.println("1. Stock Report (Low Stock)"); // Items with quantity less than 5 to add more quantity to it
      System.out.println("2. Sales Report");
      System.out.println("3. Back to Main Menu");
      System.out.print("Enter your choice: ");
      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1 -> generateStockReport();
        case 2 -> generateSalesReport();
        case 3 -> {
          return;
        }
        default -> System.out.println("Invalid choice! Try again.");
      }
    }
  }

  private static void generateStockReport() {
    System.out.println("\nLow Stock Items:");
    for (Item item : inventory.values()) {
      if (item.quantity < 5) {
        System.out.println(item);
      }
    }
  }

  private static void generateSalesReport() {
    System.out.println("\nSales History:");
    for (Sale sale : salesHistory) {
      System.out.println(sale);
    }
  }

  // Persistence
  private static void saveInventory() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
      oos.writeObject(inventory);
      System.out.println("Inventory saved successfully!");
    } catch (IOException e) {
      System.err.println("Error saving inventory: " + e.getMessage());
    }
  }
@SuppressWarnings("unchecked")
  private static void loadInventory() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INVENTORY_FILE))) {
      inventory = (Map<String, Item>) ois.readObject();
      System.out.println("Inventory loaded successfully!");
    } catch (IOException | ClassNotFoundException e) {
      inventory = new HashMap<>();
    }
  }

  private static void saveOrders() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
      oos.writeObject(orderQueue);
    } catch (IOException e) {
      System.err.println("Error saving orders: " + e.getMessage());
    }
  }
@SuppressWarnings("unchecked")
  private static void loadOrders() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDERS_FILE))) {
      orderQueue = (Queue<Order>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      orderQueue = new LinkedList<>();
    }
  }
  private static void saveSales() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SALES_FILE))) {
      oos.writeObject(salesHistory);
      System.out.println("Sales history saved successfully.");
    } catch (IOException e) {
      System.err.println("Error saving sales history: " + e.getMessage());
    }
  }
@SuppressWarnings("unchecked")
  private static void loadSales() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SALES_FILE))) {
      salesHistory = (List<Sale>) ois.readObject();
      System.out.println("Sales history loaded successfully.");
    } catch (FileNotFoundException e) {
      System.out.println("Sales file not found. Starting with an empty sales history.");
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Error loading sales history: " + e.getMessage());
      salesHistory = new ArrayList<>();
    }
  }
}
