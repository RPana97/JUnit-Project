package org.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserServiceTest {

    private UserService userService;  // Instance of UserService to test
    private User testUser;  // A sample user object used in tests

    @Before
    public void setUp() {
        // Set up the UserService and a test user before each test
        userService = new UserService();
        testUser = new User("JohnDoe", "password", "johndoe@example.com");
    }

    @Test
    public void testRegisterUser_Success() {
        // Test registering a new user; expecting it to succeed (return true)
        boolean result = userService.registerUser(testUser);
        assertTrue(result);
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        // Test trying to register the same user twice; expecting the second attempt to fail (return false)
        userService.registerUser(testUser);
        boolean result = userService.registerUser(testUser);  // Attempt to register the same user again
        assertFalse(result);
    }

    @Test
    public void testLoginUser_Success() {
        // Test logging in with correct credentials; expecting to return the user object
        userService.registerUser(testUser);
        User result = userService.loginUser("JohnDoe", "password");
        assertNotNull(result);
    }

    @Test
    public void testLoginUser_WrongPassword() {
        // Test logging in with the wrong password; expecting to return null
        userService.registerUser(testUser);
        User result = userService.loginUser("JohnDoe", "wrongpassword");
        assertNull(result);
    }

    @Test
    public void testLoginUser_NonExistentUser() {
        // Test logging in with a non-existent username; expecting to return null
        User result = userService.loginUser("NonExistentUser", "password");
        assertNull(result);
    }

    @Test
    public void testUpdateUserProfile_Success() {
        // Test updating the user's profile; expecting it to succeed (return true)
        userService.registerUser(testUser);
        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com");
        assertTrue(result);
    }

    @Test
    public void testUpdateUserProfile_UsernameAlreadyTaken() {
        // Test trying to update a user's profile to use an already taken username; expecting it to fail (return false)
        userService.registerUser(testUser);
        User anotherUser = new User("NewUsername", "password", "another@example.com");
        userService.registerUser(anotherUser);

        boolean result = userService.updateUserProfile(testUser, "NewUsername", "newpassword", "newemail@example.com");
        assertFalse(result);  // Expecting false because the new username is already taken
    }
}
