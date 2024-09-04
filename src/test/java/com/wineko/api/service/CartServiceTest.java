package com.wineko.api.service;

import com.wineko.api.model.*;
import com.wineko.api.repository.ArticleRepository;
import com.wineko.api.repository.CartRepository;
import com.wineko.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCart() {
        // Given
        Integer userId = 1;
        Users user = new Users();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedDate(new Date());

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart createdCart = cartService.createCart(userId);

        // Then
        assertNotNull(createdCart);
        assertEquals(userId, createdCart.getUser().getId());
        assertNotNull(createdCart.getCreatedDate());
    }

    @Test
    public void testAddItemToCart() {
        // Given
        Integer cartId = 1;
        Integer articleId = 1;
        Integer quantity = 2;

        Article article = new Article();
        article.setId(articleId);
        article.setPrice(10.0);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCartLines(new ArrayList<>());

        // When
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.addItemToCart(cartId, articleId, quantity);

        // Then
        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getCartLines().size());
        CartLine line = updatedCart.getCartLines().get(0);
        assertEquals(articleId, line.getArticle().getId());
        assertEquals(quantity, line.getQuantity());
        assertEquals(article.getPrice() * quantity, line.getPrice());
    }

    @Test
    public void testRemoveItemFromCart() {
        // Given
        Integer cartId = 1;
        Integer articleId = 1;

        Article article = new Article();
        article.setId(articleId);

        CartLine cartLine = new CartLine();
        cartLine.setArticle(article);
        cartLine.setQuantity(1);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.getCartLines().add(cartLine);

        // When
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.removeItemFromCart(cartId, articleId);

        // Then
        assertNotNull(updatedCart);
        assertTrue(updatedCart.getCartLines().isEmpty());
    }

    @Test
    public void testClearCart() {
        // Given
        Integer cartId = 1;

        CartLine cartLine = new CartLine();
        cartLine.setQuantity(1);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.getCartLines().add(cartLine);

        // When
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        cartService.clearCart(cartId);

        // Then
        verify(cartRepository, times(1)).save(cart);
        assertTrue(cart.getCartLines().isEmpty());
    }
}
