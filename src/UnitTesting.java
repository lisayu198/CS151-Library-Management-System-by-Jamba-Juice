package src;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.*;

public class UnitTesting {

    Library newLibrary = new Library(); //create new library object
    User newUser; //create new user object
    Book newBook; //create new book object

    /**
     * JUnit to test the addBook method in the Library class
     * test passed
     */
    @Test
    public void testAddBook(){
        newLibrary.bookInput.setText("book test"); //tester book title
        newLibrary.authorInput.setText("author test"); //tester author name
        newLibrary.isbnInput.setText("80085"); //tester isbn
        newLibrary.bookCondition.setSelectedItem(Book.CONDITION.FAIR); //tester book condition

        newLibrary.addBookAction(null); //call the addBookAction method (null b/c no need for click sensor)

        String testMethod = newLibrary.bookList.get(0); //new string to store the first value of the book list

        /**
         * assertTrue() makes sure the testMethod string has all the tester inputs
         */
        assertTrue(testMethod.contains("book test"));
        assertTrue(testMethod.contains("author test"));
        assertTrue(testMethod.contains("80085"));
        assertTrue(testMethod.contains("FAIR"));
    }

    /**
     * JUnit test to test the check out book method in the User class
     * test failed
     */
    @Test
    public void testCheckOutBook() {
        newUser = new User("test", "user", "testuser@HAHA.com", "Hello123!", "80085");
        newBook = new Book("how to survive 5 nights at freddy's", 8008135L, "josh hutcherson", Book.CONDITION.GOOD, true, 5);
        Book takenBook = new Book("how to not survive 5 nights at freddy's", 9012355L, "william afton", Book.CONDITION.POOR, false, 1);

        try{
            System.out.println("junit before: " + newBook.isAvailable());
            newUser.checkOutBook(newBook);
            System.out.println("junit after: " + newBook.isAvailable());
            assertTrue(newUser.getBorrowedBooks().contains(newBook)); //returning false for some reason
            System.out.println("Size of borrowedBooks: " + newUser.getBorrowedBooks().size());

            assertFalse(newBook.isAvailable());
        }catch (UnavailableBookException e) {
            e.printStackTrace();
            fail("error" + e.getMessage());
        }
        /*try{
            newUser.checkOutBook(takenBook);
            fail("did try/catch work? no? oh, ok");
        } catch (UnavailableBookException e) {
            assertEquals("book is taken", e.getMessage());
        }*/

    }

    /**
     * JUnit test to test the populate catalogue method in the Book class
     * test failed
     */
    @Test
    public void testPopulateCatalogue(){
        newUser = new User("test", "user", "testuser@HAHA.com", "Hello123!", "80085");
        newBook = new Book("how to survive 5 nights at freddy's", 8008135L, "josh hutcherson", Book.CONDITION.GOOD, false, 5);
        try{
            newBook.populateCatalogue(); //returns null
            assertFalse(Book.BOOKS.isEmpty());
        } catch (IOException e) {
            fail("populate failed" + e.getMessage());
        }
    }

    /**
     * JUnit test to test the write to file method in the Book class
     * test failed
     */
    @Test
    public void testWriteToFile(){
        newUser = new User("test", "user", "testuser@HAHA.com", "Hello123!", "80085");
        newBook = new Book("how to survive 5 nights at freddy's", 8008135L, "josh hutcherson", Book.CONDITION.GOOD, false, 5);
        try{
            newBook.populateCatalogue();
            Book.writeToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JUnit test to test the compare method in the Book class
     * test passes
     */
    @Test
    public void testCompare(){
        Book compareBook = new Book("fortnite battle royale", 69420L, "epic games", Book.CONDITION.GOOD, true, 2);
        assertEquals(0, Book.compare(compareBook, "fortnite battle royale"));
        assertEquals(-1, Book.compare(compareBook, "minecraft best game"));
    }

}