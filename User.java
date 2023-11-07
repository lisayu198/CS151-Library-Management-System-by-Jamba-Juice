import java.awt.print.Book;
import java.util.ArrayList;

public class User {
    String firstName;
    String lastName;
    int libraryCardNum;
    int phoneNum;
    ArrayList<Book> borrowedBooks;

    public User(String firstName, String lastName, int libraryCardNum, int phoneNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.libraryCardNum = libraryCardNum;
        this.phoneNum = phoneNum;
    }

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

    public int getLibraryCardNum() {
        return libraryCardNum;
    }

    public void setLibraryCardNum(int libraryCardNum) {
        this.libraryCardNum = libraryCardNum;
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

    // check this user with another user
    // if the same, return 0 
    // this user bigger than another user, return 1
    // otherwise, return -1
    private int compareTo(User user) {
        if(this.borrowedBooks.size() == user.borrowedBooks.size()) {
            return 0;
        }
        else if (this.borrowedBooks.size() > user.borrowedBooks.size()) {
            return 1;
        } else {
            return -1;
        }
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

}
