package org.example;

import org.junit.Before; // Annotation for setup method
import org.junit.Test; // Annotation for test methods
import org.junit.After; // Annotation for teardown method
import org.junit.BeforeClass; // Annotation for method run once before all tests
import org.junit.AfterClass; // Annotation for method run once after all tests
import org.mockito.Mockito; // Static import for Mockito methods

import java.util.Collections; // Utility class for empty and singleton lists
import java.util.List; // Import List interface for handling lists of books

import static org.junit.Assert.*; // Static import for JUnit assertions
import static org.mockito.Mockito.*; // Static import for Mockito methods like mock, when, verify, etc.

public class BookServiceTest {

    private BookService bookService;  // Mocked instance of BookService, used to isolate the test from the real implementation
    private User testUser;  // A sample user object used in tests
    private Book testBook;  // A sample book object used in tests

    @BeforeClass
    public static void beforeAllTests() {
        // This method runs once before all the test methods in this class.
        // Typically used for setting up expensive resources that are shared across tests.
        System.out.println("Starting BookService tests...");
    }

    @AfterClass
    public static void afterAllTests() {
        // This method runs once after all the test methods in this class.
        // Typically used for cleaning up resources that were set up in beforeAllTests.
        System.out.println("BookService tests completed.");
    }

    @Before
    public void setUp() {
        // This method runs before each test.
        // It sets up a fresh instance of the BookService mock, the test user, and the test book.
        bookService = mock(BookService.class); // Create a mock instance of BookService
        testUser = new User("JohnDoe", "password", "johndoe@example.com"); // Initialize a User object for use in tests
        testBook = new Book("1984", "George Orwell", "Dystopian", 9.99); // Initialize a Book object for use in tests
        when(bookService.addBook(testBook)).thenReturn(true); // Stub the addBook method to return true
        bookService.addBook(testBook); // Simulate adding the test book to the book database
    }

    @After
    public void tearDown() {
        // This method runs after each test.
        // It resets the mock to ensure each test runs independently.
        reset(bookService); // Reset the mock to its original state
    }

    @Test
    public void testSearchBook_Found() {
        // Test searching for a book by title; expecting to find it in the list

        when(bookService.searchBook("1984")).thenReturn(Collections.singletonList(testBook)); // Stub the searchBook method to return a list containing testBook
        List<Book> result = bookService.searchBook("1984"); // Call the method being tested
        assertFalse(result.isEmpty()); // Assert that the result list is not empty
        assertEquals("1984", result.get(0).getTitle()); // Assert that the title of the first book in the list matches "1984"
    }

    @Test
    public void testSearchBook_NotFound() {
        // Test searching for a non-existent book; expecting an empty list

        when(bookService.searchBook("NonExistentBook")).thenReturn(Collections.emptyList()); // Stub the searchBook method to return an empty list
        List<Book> result = bookService.searchBook("NonExistentBook"); // Call the method being tested
        assertTrue(result.isEmpty()); // Assert that the result list is empty
    }

    @Test
    public void testSearchBook_EmptyKeyword() {
        // Edge case: Test searching with an empty keyword; behavior depends on implementation

        when(bookService.searchBook("")).thenReturn(Collections.singletonList(testBook)); // Stub the searchBook method to return a list containing testBook
        List<Book> result = bookService.searchBook(""); // Call the method being tested
        assertNotNull(result); // Assert that the result list is not null
    }

    @Test
    public void testPurchaseBook_Success() {
        // Test purchasing a book that is available in the database; expecting it to succeed (return true)

        when(bookService.purchaseBook(testUser, testBook)).thenReturn(true); // Stub the purchaseBook method to return true
        boolean result = bookService.purchaseBook(testUser, testBook); // Call the method being tested
        assertTrue(result); // Assert that the result is true
    }

    @Test
    public void testPurchaseBook_BookNotInDatabase() {
        // Test attempting to purchase a book that isn't in the database; expecting it to fail (return false)

        Book nonExistentBook = new Book("Unknown", "Unknown", "Unknown", 0.0); // Create a Book object that is not in the database
        when(bookService.purchaseBook(testUser, nonExistentBook)).thenReturn(false); // Stub the purchaseBook method to return false
        boolean result = bookService.purchaseBook(testUser, nonExistentBook); // Call the method being tested
        assertFalse(result); // Assert that the result is false
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPurchaseBook_NullBook() {
        // Edge case: Test purchasing a null book; expecting an exception

        when(bookService.purchaseBook(testUser, null)).thenThrow(new IllegalArgumentException("Book cannot be null")); // Stub purchaseBook to throw an exception
        bookService.purchaseBook(testUser, null); // Call the method being tested; it should throw an IllegalArgumentException
    }

    @Test
    public void testAddBookReview_Success() {
        // Test adding a review for a book that the user has purchased; expecting it to succeed (return true)

        testUser.getPurchasedBooks().add(testBook); // Simulate that the user purchased the book by adding it to the user's purchased books
        when(bookService.addBookReview(testUser, testBook, "Great book!")).thenReturn(true); // Stub the addBookReview method to return true
        boolean result = bookService.addBookReview(testUser, testBook, "Great book!"); // Call the method being tested
        assertTrue(result); // Assert that the result is true
    }

    @Test
    public void testAddBookReview_UserDidNotPurchaseBook() {
        // Test attempting to add a review for a book that the user has not purchased; expecting it to fail (return false)

        when(bookService.addBookReview(testUser, testBook, "Great book!")).thenReturn(false); // Stub the addBookReview method to return false
        boolean result = bookService.addBookReview(testUser, testBook, "Great book!"); // Call the method being tested
        assertFalse(result); // Assert that the result is false
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBookReview_NullReview() {
        // Edge case: Test adding a null review; expecting an exception

        when(bookService.addBookReview(testUser, testBook, null)).thenThrow(new IllegalArgumentException("Review cannot be null")); // Stub addBookReview to throw an exception
        bookService.addBookReview(testUser, testBook, null); // Call the method being tested; it should throw an IllegalArgumentException
    }

    @Test
    public void testAddBook_Success() {
        // Test adding a new book to the database; expecting it to succeed (return true)

        Book newBook = new Book("New Book", "New Author", "Genre", 19.99); // Create a new Book object
        when(bookService.addBook(newBook)).thenReturn(true); // Stub the addBook method to return true
        boolean result = bookService.addBook(newBook); // Call the method being tested
        assertTrue(result); // Assert that the result is true
    }

    @Test
    public void testAddBook_BookAlreadyExists() {
        // Test attempting to add a book that already exists in the database; expecting it to fail (return false)

        when(bookService.addBook(testBook)).thenReturn(false); // Stub the addBook method to return false
        boolean result = bookService.addBook(testBook); // Attempt to add the same book again
        assertFalse(result); // Assert that the result is false
    }

    @Test
    public void testRemoveBook_Success() {
        // Test removing a book that exists in the database; expecting it to succeed (return true)

        when(bookService.removeBook(testBook)).thenReturn(true); // Stub the removeBook method to return true
        boolean result = bookService.removeBook(testBook); // Call the method being tested
        assertTrue(result); // Assert that the result is true
    }

    @Test
    public void testRemoveBook_BookNotInDatabase() {
        // Test attempting to remove a book that isn't in the database; expecting it to fail (return false)

        Book nonExistentBook = new Book("Unknown", "Unknown", "Unknown", 0.0); // Create a Book object that is not in the database
        when(bookService.removeBook(nonExistentBook)).thenReturn(false); // Stub the removeBook method to return false
        boolean result = bookService.removeBook(nonExistentBook); // Call the method being tested
        assertFalse(result); // Assert that the result is false
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveBook_NullBook() {
        // Edge case: Test removing a null book; expecting an exception

        when(bookService.removeBook(null)).thenThrow(new IllegalArgumentException("Book cannot be null")); // Stub removeBook to throw an exception
        bookService.removeBook(null); // Call the method being tested; it should throw an IllegalArgumentException
    }
}
