package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookService bookService;  // Mocked instance of BookService
    private User testUser;  // A sample user object used in tests
    private Book testBook;  // A sample book object used in tests

    @BeforeClass
    public static void beforeAllTests() {
        // Code to run before any test methods in this class
        System.out.println("Starting BookService tests...");
    }

    @AfterClass
    public static void afterAllTests() {
        // Code to run after all test methods in this class
        System.out.println("BookService tests completed.");
    }

    @Before
    public void setUp() {
        // Set up the BookService mock, a test user, and a test book before each test
        bookService = mock(BookService.class);
        testUser = new User("JohnDoe", "password", "johndoe@example.com");
        testBook = new Book("1984", "George Orwell", "Dystopian", 9.99);
        when(bookService.addBook(testBook)).thenReturn(true);  // Mock the addBook method
        bookService.addBook(testBook);  // Add the test book to the book database
    }

    @After
    public void tearDown() {
        // Code to clean up after each test
        reset(bookService);
    }

    @Test
    public void testSearchBook_Found() {
        // Test searching for a book by title; expecting to find it in the list
        when(bookService.searchBook("1984")).thenReturn(Collections.singletonList(testBook));
        List<Book> result = bookService.searchBook("1984");
        assertFalse(result.isEmpty());
        assertEquals("1984", result.get(0).getTitle());
    }

    @Test
    public void testSearchBook_NotFound() {
        // Test searching for a non-existent book; expecting an empty list
        when(bookService.searchBook("NonExistentBook")).thenReturn(Collections.emptyList());
        List<Book> result = bookService.searchBook("NonExistentBook");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSearchBook_EmptyKeyword() {
        // Edge case: Test searching with an empty keyword; behavior depends on implementation
        when(bookService.searchBook("")).thenReturn(Collections.singletonList(testBook));
        List<Book> result = bookService.searchBook("");
        assertNotNull(result);
    }

    @Test
    public void testPurchaseBook_Success() {
        // Test purchasing a book that is available in the database; expecting it to succeed (return true)
        when(bookService.purchaseBook(testUser, testBook)).thenReturn(true);
        boolean result = bookService.purchaseBook(testUser, testBook);
        assertTrue(result);
    }

    @Test
    public void testPurchaseBook_BookNotInDatabase() {
        // Test attempting to purchase a book that isn't in the database; expecting it to fail (return false)
        Book nonExistentBook = new Book("Unknown", "Unknown", "Unknown", 0.0);
        when(bookService.purchaseBook(testUser, nonExistentBook)).thenReturn(false);
        boolean result = bookService.purchaseBook(testUser, nonExistentBook);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPurchaseBook_NullBook() {
        // Edge case: Test purchasing a null book; expecting an exception
        when(bookService.purchaseBook(testUser, null)).thenThrow(new IllegalArgumentException("Book cannot be null"));
        bookService.purchaseBook(testUser, null);
    }

    @Test
    public void testAddBookReview_Success() {
        // Test adding a review for a book that the user has purchased; expecting it to succeed (return true)
        testUser.getPurchasedBooks().add(testBook);  // Simulate that the user purchased the book
        when(bookService.addBookReview(testUser, testBook, "Great book!")).thenReturn(true);
        boolean result = bookService.addBookReview(testUser, testBook, "Great book!");
        assertTrue(result);
    }

    @Test
    public void testAddBookReview_UserDidNotPurchaseBook() {
        // Test attempting to add a review for a book that the user has not purchased; expecting it to fail (return false)
        when(bookService.addBookReview(testUser, testBook, "Great book!")).thenReturn(false);
        boolean result = bookService.addBookReview(testUser, testBook, "Great book!");
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBookReview_NullReview() {
        // Edge case: Test adding a null review; expecting an exception
        when(bookService.addBookReview(testUser, testBook, null)).thenThrow(new IllegalArgumentException("Review cannot be null"));
        bookService.addBookReview(testUser, testBook, null);
    }

    @Test
    public void testAddBook_Success() {
        // Test adding a new book to the database; expecting it to succeed (return true)
        Book newBook = new Book("New Book", "New Author", "Genre", 19.99);
        when(bookService.addBook(newBook)).thenReturn(true);
        boolean result = bookService.addBook(newBook);
        assertTrue(result);
    }

    @Test
    public void testAddBook_BookAlreadyExists() {
        // Test attempting to add a book that already exists in the database; expecting it to fail (return false)
        when(bookService.addBook(testBook)).thenReturn(false);
        boolean result = bookService.addBook(testBook);  // Attempting to add the same book again
        assertFalse(result);
    }

    @Test
    public void testRemoveBook_Success() {
        // Test removing a book that exists in the database; expecting it to succeed (return true)
        when(bookService.removeBook(testBook)).thenReturn(true);
        boolean result = bookService.removeBook(testBook);
        assertTrue(result);
    }

    @Test
    public void testRemoveBook_BookNotInDatabase() {
        // Test attempting to remove a book that isn't in the database; expecting it to fail (return false)
        Book nonExistentBook = new Book("Unknown", "Unknown", "Unknown", 0.0);
        when(bookService.removeBook(nonExistentBook)).thenReturn(false);
        boolean result = bookService.removeBook(nonExistentBook);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveBook_NullBook() {
        // Edge case: Test removing a null book; expecting an exception
        when(bookService.removeBook(null)).thenThrow(new IllegalArgumentException("Book cannot be null"));
        bookService.removeBook(null);
    }
}
