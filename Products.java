package project212;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Products {
    private LinkedList<Product> products;
    private String filePath;

    public Products(LinkedList<Product> input_products) {
        products = input_products;
    }

    public Products() {
        products = new LinkedList<>();
    }

    public void setFilePath(String path) { this.filePath = path; }

    public LinkedList<Product> get_Products() { return products; }

    public Product SearchProductById(int id){
        if (products.empty()) return null;
        products.findfirst();
        while(true){
            if (products.retrieve().getProductId()==id) return products.retrieve();
            if (products.last()) break;
            products.findenext();
        }
        return null;
    }

    public void addProduct(Product p) {
        if (SearchProductById(p.getProductId())==null) {
            products.addLast(p);
            System.out.println("Product added: " + p.getName());
            saveAll();
        } else {
            System.out.println("Product with ID " + p.getProductId() + " already exists!");
        }
    }

    public void removeProduct(int id) {
        if (products.empty()) { System.out.println("Product ID not found"); return; }
        products.findfirst();
        while (true) {
            if (products.retrieve().getProductId() == id) {
                products.remove();
                System.out.println("Product removed: " + id);
                saveAll();
                return;
            }
            if (products.last()) break;
            products.findenext();
        }
        System.out.println("Product ID not found");
    }

    public void updateProduct(int id, Product p) {
        Product old=SearchProductById(id);
        if(old==null)
            System.out.println("not exist to make update");
        else {
            old.UpdateProduct(p);
            saveAll();
        }
    }

    public void displayOutOfStock() {
        System.out.println("Out of stock products:");
        if (products.empty()){
            System.out.println("no products exist");
        } else {
            boolean found = false;
            products.findfirst();
            while(true){
                if (products.retrieve().getStock()==0) {
                    System.out.println(products.retrieve().toString());
                    found=true;
                }
                if (products.last()) break;
                products.findenext();
            }
            if (!found) System.out.println("All products in stock");
        }
    }

    public void displayAllProducts() {
        System.out.println("All Products");
        if (products.empty()){
            System.out.println("no products exist");
            return ;
        } else {
            products.findfirst();
            while(true){
                Product p=products.retrieve();
                System.out.println(p.toString());
                p.displayReviews();
                if (products.last()) break;
                products.findenext();
            }
        }
    }

    public static Product convert_String_to_product(String Line) {
        String a[]=Line.split(",",4);
        Product p=new Product(Integer.parseInt(a[0].trim()), a[1].trim(),
                              Double.parseDouble(a[2].trim()),Integer.parseInt(a[3].trim()));
        return p;
    }

    public void loadProducts(String fileName) {
        try {
            filePath = fileName;
            File f = new File(fileName);
            Scanner read = new Scanner(f);
            System.out.println("Reading file: " + fileName);
            if (read.hasNextLine()) read.nextLine();
            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (!line.isEmpty()) {
                    Product  p=convert_String_to_product(line);
                    products.addLast(p);
                }
            }
            read.close();
            System.out.println("File loaded successfully!\n");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("productId,name,price,stock");
            if (!products.empty()) {
                products.findfirst();
                while (true) {
                    Product p = products.retrieve();
                    pw.println(p.getProductId()+","+p.getName()+","+p.getPrice()+","+p.getStock());
                    if (products.last()) break;
                    products.findenext();
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }
}