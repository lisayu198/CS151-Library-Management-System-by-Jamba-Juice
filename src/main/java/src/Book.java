package src;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Book {
// enum class for the condition of the book
    public enum CONDITION {
        NEW, GOOD, FAIR, POOR
    }
// variables for creating a book object; the author, title, isbn, condition, copies
    private String title;
    private boolean checkedIn;
    private String isbn;
    private String author;
    private CONDITION condition;
    private int copies;

    // Master BOOKS arraylist that holds the books inputed into the file
    public static ArrayList<Book> BOOKS = new ArrayList<Book>();

// empty constructor
    public Book() {

    }
// constructor with all the attributes
    public Book(String title, String isbn, String author, CONDITION condition, Boolean checkedIn, int copies) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.condition = condition;
        this.checkedIn = checkedIn;
        this.copies = copies;
    }
    /*
    returns title
    */
    public String getTitle() {
        return title;
    }
    /*
        sets Title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /*
    returns ISBN
     */
    public String getIsbn() {
        return isbn;
    }
    /*
    sets ISBN
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    /*
    returns author
     */
    public String getAuthor() {
        return author;
    }
    /*
    sets author
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /*
    returns checkedIn variable
     */
    public boolean isCheckedIn() {
        return this.checkedIn;
    }
    /*
    sets checkedIn attribute
     */
    public void setCheckedIn(boolean value) {
        this.checkedIn = value;
    }
    /*
    repopulates the BOOKS arraylist with the file inputs and returns the BOOKS arraylist
    throws an exception
     */
    public static ArrayList<Book> getBooks() {
        // refresh books available
        try {
            BOOKS.clear();
            Book.populateCatalogue();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return BOOKS;
    }

    /*
    We have formated a file to be in the set up of
    Title
    Author
    ISBN
    CheckedIN
    Condition
    Copies

    from there a Book file is created and stored into the BOOKS arrayList

    Creates file objects using the WelcomeScreen variable BOOKS_TXT_PATH
    uses a scanner to scan through file and
     */
    public static void populateCatalogue() throws Exception {
        BOOKS.clear();

        File file = new File(WelcomeScreen.BOOKS_TXT_PATH);

        Scanner fileInput = new Scanner(file);
        // String condition;

        while (fileInput.hasNextLine()) {
            Book newBook = new Book();

            String temp = fileInput.nextLine();
            if (temp.trim().length() == 0) {
                // one more empty line
                if (fileInput.hasNextLine()) {
                    temp = fileInput.nextLine();
                } else {
                    return;
                }
            }
            newBook.title = temp;

            newBook.author = fileInput.nextLine();

            newBook.isbn = fileInput.nextLine();

            newBook.checkedIn = Boolean.valueOf(fileInput.next());

            newBook.condition = Enum.valueOf(CONDITION.class, fileInput.next());

            fileInput.nextLine();
            newBook.copies = fileInput.nextInt();
            fileInput.nextLine();
            fileInput.nextLine();


            //Integer key = Integer.valueOf(isbn.intValue()%100);

            BOOKS.add(newBook);

        }

        fileInput.close();
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nAuthor: " + author + "\nIsbn: " + isbn + "\nCondition of Book " + condition
                + "\nChecked In: " + checkedIn + "\nCopies: " + copies;
    }

    /*
    Writes to a file using a PrintWriter and with the help of writeToFileHelper()
     */
    public static void writeToFile() throws IOException {
        File e = new File(WelcomeScreen.BOOKS_TXT_PATH);
        PrintWriter out = new PrintWriter(e);
        out.print(writeToFileHelper());
        out.close();
    }

    /*
    Reads in each book individually from the BOOKS arraylist and implements it into
    a StringBuilder in the format of
    Title
    Author
    ISBN
    CheckedIN
    Condition
    Copies

    and returns the stringBuilder to writeToFile()

     */
    private static StringBuilder writeToFileHelper() {

        StringBuilder a = new StringBuilder("");

        for (int i = 0; i < BOOKS.size(); i++) {
            a.append(BOOKS.get(i).title + "\n");
            a.append(BOOKS.get(i).author + "\n");
            a.append(BOOKS.get(i).isbn + "\n");
            a.append(String.valueOf(BOOKS.get(i).checkedIn) + "\n");
            a.append(String.valueOf(BOOKS.get(i).condition) + "\n");
            a.append(String.valueOf(BOOKS.get(i).copies) + "\n\n");

        }
        return a;
    }

    public boolean isAvailable() {
        return checkedIn;
    }

    /*
    Compares two books based on the title;
     */
    public static int compare(Book book, String title) {
        if (book.getTitle().equalsIgnoreCase(title.trim())) {
            return 0;
        } else {
            return -1;
        }
    }

    // Equals method to check if 2 book objects are equal by checking title, author, and isbn
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(title, book.getTitle()) && Objects.equals(author, book.getAuthor()) && Objects.equals(isbn, book.getIsbn());

    }

    public static void main(String[] args) throws IOException {
        Book newBook = new Book();
        try {
            newBook.populateCatalogue();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        writeToFile();
    }


}
