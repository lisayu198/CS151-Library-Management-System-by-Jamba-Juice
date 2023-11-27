package src;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Book {


    enum CONDITION {
        NEW, GOOD, FAIR, POOR
    }

    private String title;
    private static boolean checkedIn;
    private Long isbn;
    private String author;
    private CONDITION condition;
    private int copies;
    public static  ArrayList<Book> BOOKS = new ArrayList<Book>();


    public Book() {

    }

    public Book(String title, Long isbn, String author, CONDITION condition, Boolean checkedIn,int copies) {
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

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void populateCatalogue() throws IOException {

        File file = new File("C:\\Users\\anisp\\Downloads\\Library Project Nov 16\\src\\loginUI\\Books.txt");

        Scanner fileInput = new Scanner(file);
        // String condition;

        while (fileInput.hasNextLine()) {

            this.title = fileInput.nextLine();

            this.author = fileInput.nextLine();

            this.isbn = Long.valueOf(fileInput.next());

            this.checkedIn = Boolean.valueOf(fileInput.next());

            this.condition = Enum.valueOf(CONDITION.class, fileInput.next());

            fileInput.nextLine();
            this.copies = fileInput.nextInt();
            fileInput.nextLine();
            fileInput.nextLine();



            Book newBook = new Book(title, isbn, author, this.condition, checkedIn, copies);

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
    static void writeToFile() throws IOException {
        File e = new File("outFileBooks.txt");
        PrintWriter out = new PrintWriter(e);
        out.print(writeToFileHelper());
        out.close();
    }

    private static StringBuilder writeToFileHelper() {
        StringBuilder a = new StringBuilder("");

        for(int i = 0; i < BOOKS.size(); i++)
        {
            a.append(BOOKS.get(i).title + "\n");
            a.append(BOOKS.get(i).author + "\n");
            a.append(BOOKS.get(i).isbn.toString() + "\n");
            a.append(BOOKS.get(i).checkedIn + "\n");
            a.append(BOOKS.get(i).condition + "\n");
            a.append(BOOKS.get(i).copies + "\n");

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
    public void setAvailable(boolean available){
        checkedIn = available;
    }

    public static boolean isAvailable() {
        return checkedIn;

    }


    public static int compare(Book book, String title) {
        title.trim();
        System.out.println(title);
        if (book.getTitle().equalsIgnoreCase(title.trim())) {
            return 0;
        }else{
            return -1;
        }
    }


    public static void main(String[] args) throws IOException  {
        Book newBook = new Book();
        newBook.populateCatalogue();
        writeToFile();



    }


}