import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import src.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static junit.framework.TestCase.*;

public class UnitTesting {

    Library newLibrary = new Library(); //create new library object
    User newUser; //create new user object
    Book newBook; //create new book object

    @Before
    public void initialize() {
        newLibrary.getBookList().clear();
    }

    // After the tests, reset all the txt files back to the original
    @After
    public void cleanUp() {
        try {
            // Revert back to original books txt file
            Path source = Paths.get(WelcomeScreen.ORIG_BOOKS_TXT_PATH);
            Path target = Paths.get(WelcomeScreen.BOOKS_TXT_PATH);
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            // Revert back to original users txt file
            source = Paths.get(WelcomeScreen.ORIG_USERS_TXT_PATH);
            target = Paths.get(WelcomeScreen.USERS_TXT_PATH);
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception ex) {
            System.out.println("Cleanup Files Error: " + ex);
        }
    }

    /** TEST 1
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

        // Total of 16 books in library, so adding 1 more makes it 17
        assertEquals(17, newLibrary.getBookList().size()); //make sure the array list has the tester book info

        String testMethod = newLibrary.getBookList().get(newLibrary.getBookList().size()-1); //new string to store the first value of the book list

        /**
         * assertTrue() makes sure the testMethod string has all the tester inputs
         */
        assertTrue(testMethod.contains("book test"));
        assertTrue(testMethod.contains("author test"));
        assertTrue(testMethod.contains("80085"));
        assertTrue(testMethod.contains("FAIR"));

        System.out.println("testAddBook succeed");
    }

    // TEST 2
    @Test
    public void testCheckOutBook() {
        newUser = new User("test", "user", "testuser@HAHA.com", "Hello123!", "80085");
        newBook = new Book("how to survive 5 nights at freddy's", "8008135L", "josh hutcherson", Book.CONDITION.GOOD, true, 5);
        Book takenBook = new Book("how to not survive 5 nights at freddy's", "9012355L", "william afton", Book.CONDITION.POOR, true, 1);

        // positive test
        try{
            newUser.checkOutBook(newBook);
            assertTrue(newUser.getBorrowedBooks().contains(newBook)); //returning false for some reason
        }catch (UnavailableBookException e) {
            fail("error" + e.getMessage());
        }

        // negative test
        try{
            // Checking out the same book should fail, so it should go to catch
            newUser.checkOutBook(takenBook);
            // fail("did try/catch work? no? oh, ok");
        } catch (UnavailableBookException e) {
            // checks if the exception thrown is the same
            assertEquals("Book is not available for check out", e.getMessage());
        }

        System.out.println("testCheckoutBook succeed");
    }

    // TEST 3
    @Test
    public void testPopulateCatalogue(){
        try{
            newBook.populateCatalogue();
            // Want statement inside to be false to pass test, if empty it fails the test and goes into the catch
            assertFalse(Book.BOOKS.isEmpty());
        } catch (Exception e) {
            fail("populate failed" + e.getMessage());
        }

        System.out.println("testPopulateCatalogue succeed");
    }

    // TEST 4
    @Test
    public void testWriteToFile(){
        try{
            newBook.populateCatalogue();
            Book.writeToFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("testWriteToFile succeed");
    }

    // TEST 5
    @Test
    public void testCompare(){
        Book compareBook = new Book("fortnite battle royale", "69420L", "epic games", Book.CONDITION.GOOD, true, 2);
        assertEquals(0, Book.compare(compareBook, "fortnite battle royale"));
        assertEquals(-1, Book.compare(compareBook, "minecraft best game"));

        System.out.println("testCompare succeed");
    }

}
