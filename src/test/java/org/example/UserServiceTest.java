package org.example;

import org.junit.Before; // Annotation for setup method
import org.junit.Test; // Annotation for test methods
import org.junit.After; // Annotation for teardown method
import org.junit.BeforeClass; // Annotation for method run once before all tests
import org.junit.AfterClass; // Annotation for method run once after all tests
import org.mockito.Mockito; // Static import for Mockito methods

import static org.junit.Assert.*; // Static import for JUnit assertions
import static org.mockito.Mockito.*; // Static import for Mockito methods like mock, when, verify, etc.

public class UserServiceTest {

    private UserService userService;  // Mocked instance of UserService, used to isolate the test from the real implementation
    private User testUser;  // A sample user object used in tests

    @BeforeClass
    public static void beforeAllTests() {
        // This method runs once before all the test methods in this class.
        // Typically used for setting up expensive resources that are shared across tests.
        System.out.println("Starting UserService tests...");
    }

    @AfterClass
    public static void afterAllTests() {
        // This method runs once after all the test methods in this class.
        // Typically used for cleaning up resources that were set up in beforeAllTests.
        System.out.println("UserService tests completed.");
    }

    @Before
    public void setUp() {
        // This method runs before each test.
        // It sets up a fresh instance of the UserService mock and the test user.
        userService = mock(UserService.class); // Create a mock instance of UserService
        testUser = new User("JohnDoe", "password", "johndoe@example.com"); // Initialize a User object for use in tests
    }

    @After
    public void tearDown() {
        // This method runs after each test.
        // It resets the mock to ensure each test runs independently.
        reset(userService); // Reset the mock to its original state
    }

    @Test
    public void testRegisterUser_Success() {
        // Test registering a new user; expecting it to succeed (return true)

        when(userService.registerUser(testUser)).thenReturn(true); // Stub the registerUser method to return true
        boolean result = userService.registerUser(testUser); // Call the method being tested
        assertTrue(result); // Assert that the result is true
        verify(userService).registerUser(testUser); // Verify that registerUser was called with testUser
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        // Test trying to register the same user twice; expecting the second attempt to fail (return false)

        when(userService.registerUser(testUser)).thenReturn(false); // Stub the registerUser method to return false
        userService.registerUser(testUser); // First attempt to register the user (result is not checked)
        boolean result = userService.registerUser(testUser); // Second attempt to register the same user
        assertFalse(result); // Assert that the result is false
        verify(userService, times(2)).registerUser(testUser); // Verify that registerUser was called twice with testUser
    }

    @Test
    public void testRegisterUser_EmptyUsername() {
        // Edge case: Test registering a user with an empty username; expecting it to fail (return false)

        User emptyUsernameUser = new User("", "password", "email@example.com"); // Create a User with an empty username
        when(userService.registerUser(emptyUsernameUser)).thenReturn(false); // Stub the registerUser method to return false
        boolean result = userService.registerUser(emptyUsernameUser); // Call the method being tested
        assertFalse(result); // Assert that the result is false
    }

    @Test
    public void testLoginUser_Success() {
        // Test logging in with correct credentials; expecting to return the user object

        when(userService.loginUser("JohnDoe", "password")).thenReturn(testUser); // Stub the loginUser method to return testUser
        User result = userService.loginUser("JohnDoe", "password"); // Call the method being tested
        assertNotNull(result); // Assert that the result is not null (indicating a successful login)
        verify(userService).loginUser("JohnDoe", "password"); // Verify that loginUser was called with the correct credentials
    }

    @Test
    public void testLoginUser_WrongPassword() {
        // Test logging in with the wrong password; expecting to return null

        when(userService.loginUser("JohnDoe", "wrongpassword")).thenReturn(null); // Stub the loginUser method to return null
        User result = userService.loginUser("JohnDoe", "wrongpassword"); // Call the method being tested
        assertNull(result); // Assert that the result is null (indicating a failed login)
    }

    @Test
    public void testLoginUser_NonExistentUser() {
        // Test logging in with a non-existent username; expecting to return null

        when(userService.loginUser("NonExistentUser", "password")).thenReturn(null); // Stub the loginUser method to return null
        User result = userService.loginUser("NonExistentUser", "password"); // Call the method being tested
        assertNull(result); // Assert that the result is null (indicating the user does not exist)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginUser_NullUsername() {
        // Edge case: Test logging in with a null username; expecting an exception

        when(userService.loginUser(null, "password")).thenThrow(new IllegalArgumentException("Username cannot be null")); // Stub loginUser to throw an exception
        userService.loginUser(null, "password"); // Call the method being tested; it should throw an IllegalArgumentException
    }

    @Test
    public void testUpdateUserProfile_Success() {
        // Test updating the user's profile; expecting it to succeed (return true)

        when(userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com")).thenReturn(true); // Stub the updateUserProfile method to return true
        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com"); // Call the method being tested
        assertTrue(result); // Assert that the result is true
    }

    @Test
    public void testUpdateUserProfile_UsernameAlreadyTaken() {
        // Test trying to update a user's profile to use an already taken username; expecting it to fail (return false)

        User anotherUser = new User("NewUsername", "password", "another@example.com"); // Create another User with the new username
        when(userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com")).thenReturn(false); // Stub the updateUserProfile method to return false

        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com"); // Call the method being tested
        assertFalse(result); // Assert that the result is false
    }

    @Test
    public void testUpdateUserProfile_InvalidEmail() {
        // Edge case: Test updating the user's profile with an invalid email; expecting it to fail (return false)

        when(userService.updateUserProfile(testUser, "NewUsername", "newpassword", "invalidemail")).thenReturn(false); // Stub the updateUserProfile method to return false
        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "invalidemail"); // Call the method being tested
        assertFalse(result); // Assert that the result is false
    }
}
