package com.wineko.api.service;

import com.wineko.api.model.Users;
import com.wineko.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmail() {
        // Given
        String email = "test@example.com";
        Users user = new Users();
        user.setEmail(email);

        // When
        when(userRepository.findByEmail(email)).thenReturn(user);
        Users found = usersService.findByEmail(email);

        // Then
        assertEquals(email, found.getEmail());
    }

    @Test
    public void testUpdateUser() {
        // Given
        Integer userId = 1;
        Users existingUser = new Users();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");
        existingUser.setName("Old Name");

        Users updatedUser = new Users();
        updatedUser.setEmail("new@example.com");
        updatedUser.setName("New Name");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // When
        Users result = usersService.update(userId, updatedUser);

        // Then
        assertEquals("new@example.com", result.getEmail());
        assertEquals("New Name", result.getName());
    }

    @Test
    public void testGetCurrentUserId() {
        // Given
        String email = "current@example.com";
        Users user = new Users();
        user.setEmail(email);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        Users result = usersService.getCurrentUserId();

        // Then
        assertEquals(email, result.getEmail());
    }

}