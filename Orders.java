package project212;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Orders {
    private LinkedList<Order> all_orders;
    private Customers all_Customers;
    private String filePath; // للحفظ التلقائي

    static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Orders(LinkedList<Customer> input_customers, LinkedList<Order> all_orders) {
        all_Customers = new Customers(input_customers);
        this.all_orders = all_orders;
    }

    public Orders() {
        all_Customers = new Customers();
        all_orders = new LinkedList<>();
    }

    public void setFilePath(String path) { this.filePath = path; }

    public LinkedList<Order> get_Orders() { return all_orders; }

    // Time Complexity: O(n)
    public Order searchOrderById(int id) {
        if (all_orders.empty()) return null;
        all_orders.findfirst();
        while (true) {
            Order o = all_orders.retrieve();
            if (o.getOrderId() == id) return o;
            if (all_orders.last()) break;
            all_orders.findenext();
        }
        return null;
    }

    // Time Complexity: O(n) للبحث عن العميل
    public void assign(Order ord) {
        Customer p = all_Customers.searchById(ord.getCustomerId());
        if (p == null)
            System.out.println("Customer " + ord.getCustomerId() + " does not exist to assign the order.");
        else
            p.addOrder(ord);
    }

    // Time Complexity: O(n)
    public void addOrder(Order ord) {
        if (searchOrderById(ord.getOrderId()) == null) {
            all_orders.addLast(ord);
            assign(ord);
            saveAll(); // حفظ تلقائي
        } else {
            System.out.println("Order with ID " + ord.getOrderId() + " already exists!");
        }
    }

    // حذف حسب ID - Time Complexity: O(n)
    public void removeOrderById(int id) {
        if (all_orders.empty()) { System.out.println("Order ID not found"); return; }
        all_orders.findfirst();
        while (true) {
            if (all_orders.retrieve().getOrderId() == id) {
                all_orders.remove();
                System.out.println("Order removed: " + id);
                saveAll(); // حفظ تلقائي
                return;
            }
            if (all_orders.last()) break;
            all_orders.findenext();
        }
        System.out.println("Order ID not found");
    }

    // Time Complexity: O(1) parsing
    public static Order convert_String_to_order(String line) {
        String a[] = line.split(",", 6);
        String s0 = a[0].trim().replace("\"",""); // orderId
        String s1 = a[1].trim().replace("\"",""); // customerId
        String s2 = a[2].trim().replace("\"",""); // productIds (semicolon separated)
        String s3 = a[3].trim().replace("\"",""); // totalPrice
        String s4 = a[4].trim().replace("\"",""); // date yyyy-MM-dd
        String s5 = a[5].trim().replace("\"",""); // status

        int orderId = Integer.parseInt(s0);
        int customerId = Integer.parseInt(s1);
        String productIds = s2;
        double totalPrice = Double.parseDouble(s3);
        LocalDate date = LocalDate.parse(s4, df);
        String status = s5;

        return new Order(orderId, customerId, productIds, totalPrice, date, status);
    }

    // Time Complexity: O(n)
    public void loadOrders(String fileName) {
        try {
            filePath = fileName;
            File f = new File(fileName);
            Scanner read = new Scanner(f);
            System.out.println("Reading file: " + fileName);
            System.out.println("-----------------------------------");
            if (read.hasNextLine()) read.nextLine(); // skip header
            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (line.isEmpty()) continue;
                Order ord = convert_String_to_order(line);
                all_orders.addLast(ord);
            }
            read.close();
            System.out.println("File loaded successfully!\n");
        } catch (Exception e) {
            System.out.println("Error loading all_orders: " + e.getMessage());
        }
    }

    // Time Complexity: O(n)
    public void displayAllOrders() {
        if (all_orders.empty()) {
            System.out.println(" No orders found!");
            return;
        }

        System.out.println("OrderID\tCustomerID\tProductIDs\t\tTotalPrice\tDate\t\tStatus");
        System.out.println("--------------------------------------------------------------------------");

        all_orders.findfirst();
        while (true) {
            Order o = all_orders.retrieve();
            System.out.println(o);
            if (all_orders.last()) break;
            all_orders.findenext();
        }

        System.out.println("--------------------------------------------------------------------------");
    }

    // حفظ تلقائي لكل الطلبات في CSV
    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("orderId,customerId,productIds,totalPrice,date,status");
            if (!all_orders.empty()) {
                all_orders.findfirst();
                while (true) {
                    Order o = all_orders.retrieve();
                    pw.println(o.getOrderId()+","+o.getCustomerId()+","+o.getProd_Ids()+","+o.getTotalPrice()+","+o.getOrderDate().toString()+","+o.getStatus());
                    if (all_orders.last()) break;
                    all_orders.findenext();
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }
}