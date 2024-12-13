import java.util.*;

public class InventoryManagementSystem {

    // HashMap to store item stock with item name as the key and stock as the value
    private final Map<String, Item> stock;

    // Queue to process customer orders
    private final Queue<Order> orders;

    // Constructor
    public InventoryManagementSystem() {
        this.stock = new HashMap<>();
        this.orders = new LinkedList<>();
    }

    // Method to add or update stock
    public void addOrUpdateStock(String itemName, int quantity, double price) {
        Item item = stock.getOrDefault(itemName, new Item(itemName, 0, price));
        item.setStock(item.getStock() + quantity);
        stock.put(itemName, item);
        System.out.println(quantity + " units of " + itemName + " added/updated in stock.");
    }

    // Method to process an order
    public void placeOrder(String customerName, Map<String, Integer> orderItems) {
        Order order = new Order(customerName, orderItems);
        orders.offer(order);
        System.out.println("Order placed by " + customerName + " has been added to the queue.");
    }

    // Method to process the next order in the queue
    public void processNextOrder() {
        if (orders.isEmpty()) {
            System.out.println("No orders to process.");
            return;
        }

        Order order = orders.poll();
        System.out.println("Processing order for: " + order.getCustomerName());

        boolean allItemsAvailable = true;
        double totalCost = 0.0;

        for (Map.Entry<String, Integer> entry : order.getOrderItems().entrySet()) {
            String item = entry.getKey();
            int quantity = entry.getValue();

            Item stockItem = stock.get(item);
            if (stockItem == null || stockItem.getStock() < quantity) {
                System.out.println("Insufficient stock for item: " + item);
                allItemsAvailable = false;
            } else {
                totalCost += stockItem.getPrice() * quantity;
            }
        }

        if (allItemsAvailable) {
            for (Map.Entry<String, Integer> entry : order.getOrderItems().entrySet()) {
                String item = entry.getKey();
                int quantity = entry.getValue();
                Item stockItem = stock.get(item);
                stockItem.setStock(stockItem.getStock() - quantity);
            }
            System.out.println("Order for " + order.getCustomerName() + " has been processed successfully.");
            System.out.println("Total Cost: $" + totalCost);
        } else {
            System.out.println("Order for " + order.getCustomerName() + " cannot be processed due to insufficient stock.");
        }
    }

    // Method to generate a detailed stock report
    public void generateStockReport() {
        System.out.println("\n--- Stock Report ---");
        for (Item item : stock.values()) {
            System.out.println("Item: " + item.getName() + ", Stock: " + item.getStock() + ", Price: $" + item.getPrice());
        }
    }

    // Method to display menu options
    private void displayMenu() {
        System.out.println("\n--- Inventory Management System ---");
        System.out.println("1. Add/Update Stock");
        System.out.println("2. Place Order");
        System.out.println("3. Process Next Order");
        System.out.println("4. Generate Stock Report");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    // Main method to run the CLI
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter item name: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    addOrUpdateStock(itemName, quantity, price);
                }
                case 2 -> {
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    Map<String, Integer> orderItems = new HashMap<>();
                    System.out.print("Enter number of items in the order: ");
                    int itemCount = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < itemCount; i++) {
                        System.out.print("Enter item name: ");
                        String itemName = scanner.nextLine();
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        orderItems.put(itemName, quantity);
                    }
                    placeOrder(customerName, orderItems);
                }
                case 3 -> processNextOrder();
                case 4 -> generateStockReport();
                case 5 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // Inner class to represent an item
    private static class Item {
        private final String name;
        private int stock;
        private final double price;

        public Item(String name, int stock, double price) {
            this.name = name;
            this.stock = stock;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public double getPrice() {
            return price;
        }
    }

    // Inner class to represent an order
    private static class Order {
        private final String customerName;
        private final Map<String, Integer> orderItems;

        public Order(String customerName, Map<String, Integer> orderItems) {
            this.customerName = customerName;
            this.orderItems = orderItems;
        }

        public String getCustomerName() {
            return customerName;
        }

        public Map<String, Integer> getOrderItems() {
            return orderItems;
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        InventoryManagementSystem ims = new InventoryManagementSystem();
        ims.run();
    }
}
