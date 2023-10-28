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
    public void updateBorrowedBooks(ArrayList<Book> list, Book book) {
        list.add(book);
    }

    // check if it's the correct user
    private boolean compareTo(User user) {
        return false;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }




}
