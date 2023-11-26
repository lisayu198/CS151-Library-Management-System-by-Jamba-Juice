package src;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Book {

    public enum CONDITION {
        NEW, GOOD, FAIR, POOR
    }

    private String title;
    private boolean checkedIn;
    private String isbn;
    private String author;
    private CONDITION condition;
    private int copies;
    public static ArrayList<Book> BOOKS = new ArrayList<Book>();


    public Book() {

    }

    public Book(String title, String isbn, String author, CONDITION condition, Boolean checkedIn, int copies) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.condition = condition;
        this.checkedIn = checkedIn;
        this.copies = copies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isCheckedIn() {
        return this.checkedIn;
    }

    public void setCheckedIn(boolean value) {
        this.checkedIn = value;
    }

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


    public static void populateCatalogue() throws Exception {

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

    /*public static void iterateUsingForEach(Map<Integer, bookArrayList> BOOK) {
        for (Map.Entry<Integer, bookArrayList> entry : BOOK.entrySet()) {
            Integer key = entry.getKey();
            System.out.println(BOOK.get(key).toString() + "\n");

        }
    }
*/
    public static void writeToFile() throws IOException {
        File e = new File(WelcomeScreen.BOOKS_TXT_PATH);
        PrintWriter out = new PrintWriter(e);
        out.print(writeToFileHelper());
        out.close();
    }

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

		/*	for (Map.Entry<Integer, bookArrayList> entry : BOOKS.entrySet()) {
				Integer key = entry.getKey();

				a.append(BOOKS.get(key).title + "\n");

				a.append(BOOKS.get(key).author + "\n");

				a.append(BOOKS.get(key).isbn.toString() + "\n");

				a.append(String.valueOf(BOOKS.get(key).checkedIn) + "\n");

				a.append(String.valueOf(BOOKS.get(key).condition) + "\n");

				a.append(String.valueOf(BOOKS.get(key).copies) + "\n\n");


			}*/

        return a;
    }

    public static boolean isAvailable() {
        return false;

    }


    public static int compare(Book book, String title) {
        title.trim();
        System.out.println(title);
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
