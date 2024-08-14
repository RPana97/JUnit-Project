package org.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class BookServiceTest {

    private BookService bookService;  // Instance of BookService to test
    private User testUser;  // A sample user object used in tests
    private Book testBook;  // A sample book object used in tests

    @Before
    public void setUp() {
        // Set up the BookService, a test user, and a test book before each test
        bookService = new BookService();
        testUser = new User("JohnDoe", "password", "johndoe@example.com");
        testBook = new Book("1984", "George Orwell", "Dystopian", 9.99);
        bookService.addBook(testBook);  // Add the test book to the book database
    }

    @Test
    public void testSearchBook_Found() {
        // Test searching for a book by title; expecting to find it in the list
        List<Book> result = bookService.searchBook("1984");
        assertFalse(result.isEmpty());  // Check that the result is not empty
        assertEquals("1984", result.get(0).getTitle());  // Check that the title matches
    }

    @Test
    public void testSearchBook_NotFound() {
        // Test searching for a non-existent book; expecting an empty list
        List<Book> result = bookService.searchBook("NonExistentBook");
        assertTrue(result.isEmpty());  // Expecting an empty list as no books match the keyword
    }

    @Test
    public void testSearchBook_EmptyKeyword() {
        // Test searching with an empty keyword; behavior depends on implementation (e.g., may return all or no books)
        List<Book> result = bookService.searchBook("");
        assertNotNull(result);  // Ensure the result is not null, behavior-specific
    }

    @Test
    public void testPurchaseBook_Success() {
        // Test purchasing a book that is available in the database; expecting it to succeed (return true)
        boolean result = bookService.purchaseBook(testUser, testBook);
        assertTrue(result);
    }

    @Test
    public void testPurchaseBook_BookNotInDatabase() {
        // Test attempting to purchase a book that isn't in the database; expecting it to fail (return false)
        Book nonExistentBook = new Book("Unknown", "Unknown", "Unknown", 0.0);
        boolean result = bookService.purchaseBook(testUser, nonExistentBook);
        assertFalse(result);
    }

    @Test
    public void testAddBookReview_Success() {
        // Test adding a review for a book that the user has purchased; expecting it to succeed (return true)
        testUser.getPurchasedBooks().add(testBook);  // Simulate that the user purchased the book
        boolean result = bookService.addBookReview(testUser, testBook, "Great book!");
        assertTrue(result);
    }

    @Test
    public void testAddBookReview_UserDidNotPurchaseBook() {
        // Test attempting to add a review for a book that the user has not purchased; expecting it to fail (return false)
        boolean result = bookService.addBookReview(testUser, testBook, "Great book!");
        assertFalse(result);
    }

    @Test
    public void testAddBook_Success() {
        // Test adding a new book to the database; expecting it to succeed (return true)
        Book newBook = new Book("New Book", "New Author", "Genre", 19.99);
        boolean result = bookService.addBook(newBook);
        assertTrue(result);
    }

    @Test
    public void testAddBook_BookAlreadyExists() {
        // Test attempting to add a book that already exists in the database; expecting it to fail (return false)
        boolean result = bookService.addBook(testBook);  // Attempting to add the same book again
        assertFalse(result);
    }

    @Test
    public void testRemoveBook_Success() {
        // Test removing a book that exists in the database; expecting it to succeed (return true)
        boolean result = bookService.removeBook(testBook);
        assertTrue(result);
    }

    @Test
    public void testRemoveBook_BookNotInDatabase() {
        // Test attempting to remove a book that isn't in the database; expecting it to fail (return false)
        Book nonExistentBook = new Book("Unknown", "Unknown", "Unknown", 0.0);
        boolean result = bookService.removeBook(nonExistentBook);
        assertFalse(result);
    }
}
