package src;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.*;

public class UnitTesting {

    Library newLibrary = new Library();
    User newUser;
    Book newBook;

    @Test
    public void testAddBook(){
        assertEquals(0, newLibrary.bookList.size());

        newLibrary.bookInput.setText("book test");
        newLibrary.authorInput.setText("author test");
        newLibrary.isbnInput.setText("80085");
        newLibrary.bookCondition.setSelectedItem(Book.CONDITION.FAIR);

        newLibrary.addBookAction(null);

        assertEquals(1, newLibrary.bookList.size());

        String testMethod = newLibrary.bookList.get(0);
        assertTrue(testMethod.contains("book test"));
        assertTrue(testMethod.contains("author test"));
        assertTrue(testMethod.contains("80085"));
        assertTrue(testMethod.contains("FAIR"));
    }

    @Test
    public void testCheckOutBook() throws UnavailableBookException {
        newUser = new User("test", "user", "testuser@HAHA.com", "Hello123!", "80085");
        newBook = new Book("how to survive 5 nights at freddy's", 8008135L, "josh hutcherson", Book.CONDITION.GOOD, false, 5);
        Book takenBook = new Book("how to not survive 5 nights at freddy's", 9012355L, "william afton", Book.CONDITION.POOR, true, 1);

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

    }

    @Test
    public void testPopulateCatalogue(){
        try{
            newBook.populateCatalogue(); //returns null
            assertFalse(Book.BOOKS.isEmpty());
        } catch (IOException e) {
            fail("populate failed" + e.getMessage());
        }
    }

}