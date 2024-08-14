package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;  // Mocked instance of UserService
    private User testUser;  // A sample user object used in tests

    @BeforeClass
    public static void beforeAllTests() {
        // Code to run before any test methods in this class
        System.out.println("Starting UserService tests...");
    }

    @AfterClass
    public static void afterAllTests() {
        // Code to run after all test methods in this class
        System.out.println("UserService tests completed.");
    }

    @Before
    public void setUp() {
        // Set up the UserService mock and a test user before each test
        userService = mock(UserService.class);
        testUser = new User("JohnDoe", "password", "johndoe@example.com");
    }

    @After
    public void tearDown() {
        // Code to clean up after each test
        reset(userService);
    }

    @Test
    public void testRegisterUser_Success() {
        // Test registering a new user; expecting it to succeed (return true)
        when(userService.registerUser(testUser)).thenReturn(true);
        boolean result = userService.registerUser(testUser);
        assertTrue(result);
        verify(userService).registerUser(testUser);
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        // Test trying to register the same user twice; expecting the second attempt to fail (return false)
        when(userService.registerUser(testUser)).thenReturn(false);
        userService.registerUser(testUser);  // First attempt
        boolean result = userService.registerUser(testUser);  // Second attempt
        assertFalse(result);
        verify(userService, times(2)).registerUser(testUser);
    }

    @Test
    public void testRegisterUser_EmptyUsername() {
        // Edge case: Test registering a user with an empty username; expecting it to fail (return false)
        User emptyUsernameUser = new User("", "password", "email@example.com");
        when(userService.registerUser(emptyUsernameUser)).thenReturn(false);
        boolean result = userService.registerUser(emptyUsernameUser);
        assertFalse(result);
    }

    @Test
    public void testLoginUser_Success() {
        // Test logging in with correct credentials; expecting to return the user object
        when(userService.loginUser("JohnDoe", "password")).thenReturn(testUser);
        User result = userService.loginUser("JohnDoe", "password");
        assertNotNull(result);
        verify(userService).loginUser("JohnDoe", "password");
    }

    @Test
    public void testLoginUser_WrongPassword() {
        // Test logging in with the wrong password; expecting to return null
        when(userService.loginUser("JohnDoe", "wrongpassword")).thenReturn(null);
        User result = userService.loginUser("JohnDoe", "wrongpassword");
        assertNull(result);
    }

    @Test
    public void testLoginUser_NonExistentUser() {
        // Test logging in with a non-existent username; expecting to return null
        when(userService.loginUser("NonExistentUser", "password")).thenReturn(null);
        User result = userService.loginUser("NonExistentUser", "password");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginUser_NullUsername() {
        // Edge case: Test logging in with a null username; expecting an exception
        when(userService.loginUser(null, "password")).thenThrow(new IllegalArgumentException("Username cannot be null"));
        userService.loginUser(null, "password");
    }

    @Test
    public void testUpdateUserProfile_Success() {
        // Test updating the user's profile; expecting it to succeed (return true)
        when(userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com")).thenReturn(true);
        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com");
        assertTrue(result);
    }

    @Test
    public void testUpdateUserProfile_UsernameAlreadyTaken() {
        // Test trying to update a user's profile to use an already taken username; expecting it to fail (return false)
        User anotherUser = new User("NewUsername", "password", "another@example.com");
        when(userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com")).thenReturn(false);

        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com");
        assertFalse(result);
    }

    @Test
    public void testUpdateUserProfile_InvalidEmail() {
        // Edge case: Test updating the user's profile with an invalid email; expecting it to fail (return false)
        when(userService.updateUserProfile(testUser, "NewUsername", "newpassword", "invalidemail")).thenReturn(false);
        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "invalidemail");
        assertFalse(result);
    }
}
