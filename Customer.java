package project212;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private LinkedList<Order> orders;
    private LinkedList<Review> reviews;

    // Time Complexity: O(1)
    public Customer(int id, String name, String email) {
        this.customerId = id;
        this.name = name;
        this.email = email;
        this.orders = new LinkedList<>();
        this.reviews = new LinkedList<>();
    }
// Time Complexity: O(1)
    public int getCustomerId() { return customerId; }
    // Time Complexity: O(1)
    public String getName() { return name; }
    // Time Complexity: O(1)
    public String getEmail() { return email; }

    // Time Complexity: O(n)
    public void addOrder(Order o) { orders.addLast(o); }
    public void addReview(Review r) { reviews.addLast(r); }

    // Time Complexity: O(n)
    public void displayReviews() {
        System.out.println("Reviews for " + name + ":");
        if (reviews.empty()) {
            System.out.println("  No reviews yet.");
        } else {
            reviews.findfirst();
            while (true) {
                System.out.println(reviews.retrieve().toString());
                if (reviews.last()) break;
                reviews.findenext();
            }
        }
    }

    // Time Complexity: O(n)
    public void display() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("-----------------------------------");
        displayOrders();
        displayReviews();
    }
    
// Time Complexity: O(n)
    public void displayOrders() {
        if (orders.empty()) {
            System.out.println("No orders for customer " + name);
            return;
        }
        System.out.println("Orders for " + name + ":");
        orders.findfirst();
        while (true) {
            System.out.println(orders.retrieve().toString());
            if (orders.last()) break;
            orders.findenext();
        }
    }

}
