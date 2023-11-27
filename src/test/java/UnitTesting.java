import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import src.Book;
import src.Library;
import src.UnavailableBookException;
import src.User;

import static junit.framework.TestCase.*;

public class UnitTesting {

    Library newLibrary = new Library(); //create new library object
    User newUser; //create new user object
    Book newBook; //create new book object

    @Before
    public void initialize() {
        newLibrary.getBookList().clear();
    }

    @After
    public void cleanUp() {
        // newLibrary.getBookList()
    }

    /**
     * JUnit to test the addBook method in the Library class
     */
    @Test
    public void testAddBook(){
        assertEquals(0, newLibrary.getBookList().size()); //make sure the array list is empty

        newLibrary.bookInput.setText("book test"); //tester book title
        newLibrary.authorInput.setText("author test"); //tester author name
        newLibrary.isbnInput.setText("80085"); //tester isbn
        newLibrary.bookCondition.setSelectedItem(Book.CONDITION.FAIR); //tester book condition

        newLibrary.addBookAction(null); //call the addBookAction method (null b/c no need for click sensor)

        assertEquals(1, newLibrary.getBookList().size()); //make sure the array list has the tester book info

        String testMethod = newLibrary.getBookList().get(0); //new string to store the first value of the book list

        /**
         * assertTrue() makes sure the testMethod string has all the tester inputs
         */
        assertTrue(testMethod.contains("book test"));
        assertTrue(testMethod.contains("author test"));
        assertTrue(testMethod.contains("80085"));
        assertTrue(testMethod.contains("FAIR"));

        System.out.println("testAddBook passed");
    }

    @Test
    public void testCheckOutBook() {
        newUser = new User("test", "user", "testuser@HAHA.com", "Hello123!", "80085");
        newBook = new Book("how to survive 5 nights at freddy's", "8008135L", "josh hutcherson", Book.CONDITION.GOOD, false, 5);
        Book takenBook = new Book("how to not survive 5 nights at freddy's", "9012355L", "william afton", Book.CONDITION.POOR, true, 1);

        try{
            newUser.checkOutBook(newBook);
            assertTrue(newUser.getBorrowedBooks().contains(newBook)); //returning false for some reason
        }catch (UnavailableBookException e) {
            fail("error" + e.getMessage());
        }
        try{
            newUser.checkOutBook(takenBook);
            fail("did try/catch work? no? oh, ok");
        } catch (UnavailableBookException e) {
            assertEquals("book is taken", e.getMessage());
        }

        System.out.println("testCheckoutBook succeed");
    }

    @Test
    public void testPopulateCatalogue(){
        try{
            newBook.populateCatalogue(); //returns null
            assertFalse(Book.BOOKS.isEmpty());
        } catch (Exception e) {
            fail("populate failed" + e.getMessage());
        }

        System.out.println("testPopulateCatalogue succeed");
    }

//    @Test
//    public void testWriteToFile(){
//        try{
//            newBook.populateCatalogue();
//            Book.writeToFile();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    public void testCompare(){
        Book compareBook = new Book("fortnite battle royale", "69420L", "epic games", Book.CONDITION.GOOD, true, 2);
        assertEquals(0, Book.compare(compareBook, "fortnite battle royale"));
        assertEquals(-1, Book.compare(compareBook, "minecraft best game"));

        System.out.println("testCompare succeed");
    }

}
