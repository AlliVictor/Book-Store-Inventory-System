import java.io.*;
import java.util.*;

class Book {
    private String title;
    private String author;
    private int quantity;

    public Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
        }
    }

    public boolean isInStock() {
        return this.quantity > 0;
    }
}

class BookStore {
    private List<Book> inventory;

    public BookStore() {
        this.inventory = new ArrayList<>();
    }

    // Add a book with a specified quantity
    public void addBook(String title, String author, int quantity) {
        // Check if the book is already in the inventory
        for (Book book : inventory) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                book.increaseQuantity(quantity);  // Increase quantity if already in stock
                System.out.println("Increased quantity of '" + title + "' by " + quantity);
                return;
            }
        }

        // If the book is not in the inventory, add a new book with the specified quantity
        Book book = new Book(title, author, quantity);
        inventory.add(book);
        System.out.println("Added " + quantity + " copies of '" + title + "' to the inventory.");
    }

    public void searchBook(String keyword) {
        List<Book> searchResults = new ArrayList<>();

        for (Book book : inventory) {
            if (book.getTitle().contains(keyword) || book.getAuthor().contains(keyword)) {
                searchResults.add(book);
            }
        }

        if (searchResults.isEmpty()) {
            System.out.println("No books found matching the search keyword.");
        } else {
            System.out.println("Search results:");
            for (Book book : searchResults) {
                System.out.println(book.getTitle() + " by " + book.getAuthor() + " (" + book.getQuantity() + " in stock)");
            }
        }
    }

    public void sellBook(String title) {
        for (Book book : inventory) {
            if (book.getTitle().equals(title)) {
                if (book.getQuantity() > 0) {
                    book.decreaseQuantity(1);
                    System.out.println("Book '" + title + "' sold successfully.");
                } else {
                    System.out.println("Book '" + title + "' is out of stock.");
                }
                return;
            }
        }
        System.out.println("Book '" + title + "' not found in the inventory.");
    }

    public void restockBook(String title, int quantity) {
        for (Book book : inventory) {
            if (book.getTitle().equals(title)) {
                book.increaseQuantity(quantity);
                System.out.println("Restocked " + quantity + " copies of '" + title + "'.");
                return;
            }
        }
        System.out.println("Book '" + title + "' not found in the inventory.");
    }

    public void saveInventoryToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (Book book : inventory) {
                writer.println(book.getTitle() + "," + book.getAuthor() + "," + book.getQuantity());
            }
            System.out.println("Inventory saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving inventory to file: " + e.getMessage());
        }
    }

    public void loadInventoryFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            inventory.clear();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String title = parts[0];
                String author = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                Book book = new Book(title, author, quantity);
                inventory.add(book);
            }
            System.out.println("Inventory loaded from file: " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }
}

public class BookStoreInventorySystem {
    public static void main(String[] args) {
        BookStore bookstore = new BookStore();

        bookstore.loadInventoryFromFile("src/catalog.txt");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("----- Bookstore Inventory System -----");
            System.out.println("1. Add a book");
            System.out.println("2. Search for books");
            System.out.println("3. Sell a book");
            System.out.println("4. Restock a book");
            System.out.println("0. Exit\n");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                System.out.println();

                switch (choice) {
                    case 1:
                        System.out.print("Enter the title of the book: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter the author of the book: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter the quantity of the book: ");
                        int quantity = scanner.nextInt();
                        bookstore.addBook(title, author, quantity);
                        System.out.println("Book added successfully.\n");
                        break;
                    case 2:
                        System.out.print("Enter the search keyword: ");
                        String keyword = scanner.nextLine();
                        bookstore.searchBook(keyword);
                        System.out.println();
                        break;
                    case 3:
                        System.out.print("Enter the title of the book to sell: ");
                        String sellTitle = scanner.nextLine();
                        bookstore.sellBook(sellTitle);
                        System.out.println();
                        break;
                    case 4:
                        System.out.print("Enter the title of the book to restock: ");
                        String restockTitle = scanner.nextLine();
                        System.out.print("Enter the quantity to restock: ");
                        int restockQuantity = scanner.nextInt();
                        bookstore.restockBook(restockTitle, restockQuantity);
                        System.out.println();
                        break;
                    case 0:
                        bookstore.saveInventoryToFile("src/catalog.txt");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.\n");
                }
            } else {
                System.out.println("Invalid input. Please try again.\n");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
}