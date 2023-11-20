package src.loginUI;


import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String userid;
    private String password;
    private int libraryCardNum;
    private ArrayList<Book> borrowedBooks;


    public User(String firstName, String lastName, String email, String password, int libraryCardNum) {
        super();

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.libraryCardNum = libraryCardNum;
    }

    public User() {
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getLibraryCardNum() {
        return libraryCardNum;
    }

    public void setLibraryCardNum(int num) {
        this.libraryCardNum = num;
    }

    // add the new book passed in into the arraylist
    // "checking out a book from the library"
    public void checkOutBook(Book book) throws UnavailableBookException {
        try {
            if (!book.isAvailable()) {
                throw new UnavailableBookException("Book is not available for check out");
            }
            // code to check out the book
        } catch (UnavailableBookException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // remove book that is returned
    // "returning a book back to the library"
    public void removeBorrowedBooks(ArrayList<Book> list, Book book) {
        list.remove(book);
    }

    public ArrayList<Book> getBorrowedBooks() {
        if (this.borrowedBooks == null) {
            this.borrowedBooks = new ArrayList<>();
        }
        return this.borrowedBooks;
    }

    // check this user with another user
    // if the same, return 0
    // this user bigger than another user, return 1
    // otherwise, return -1
    private int compareTo(User user) {
        if (this.borrowedBooks.size() == user.borrowedBooks.size()) {
            return 0;
        } else if (this.borrowedBooks.size() > user.borrowedBooks.size()) {
            return 1;
        } else {
            return -1;
        }
    }
}
