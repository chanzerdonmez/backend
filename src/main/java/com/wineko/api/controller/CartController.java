package com.wineko.api.controller;

import com.wineko.api.manager.JwtTokenManager;
import com.wineko.api.model.Cart;
import com.wineko.api.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    @Autowired
    private final CartService cartService;

    @Autowired
    private final JwtTokenManager jwtTokenManager;

    @Autowired
    public CartController(CartService cartService, JwtTokenManager jwtTokenManager) {
        this.cartService = cartService;
        this.jwtTokenManager = jwtTokenManager;
    }



    // Create a new cart
    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@RequestParam Integer userId) {
//        logger.info("Création d'un nouveau panier pour l'utilisateur ID: " + userId);
        Cart cart = cartService.createCart(userId);
        return ResponseEntity.ok(cart);
    }


    // Add item to cart
    @PostMapping("/{cartId}/items")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable Integer cartId,
            @RequestBody Map<String, Integer> payload,
            @CookieValue(value = "token", defaultValue = "") String token) {

        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId;
        try {
            // Extraction de l'ID utilisateur à partir du JWT
            userId = jwtTokenManager.getUserIdFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer articleId = payload.get("articleId");
        Integer quantity = payload.get("quantity");

        // Ajout d'un log pour déboguer les valeurs reçues
        System.out.println("articleId: " + articleId + ", quantity: " + quantity);

        // Vérification que les valeurs reçues ne sont pas nulles
        if (articleId == null || quantity == null) {
            System.out.println("articleId ou quantity est null");
            return ResponseEntity.badRequest().build();
        }

        // Vérifiez si le panier appartient bien à l'utilisateur
        Cart cart = cartService.getCartById(cartId);
        if (!cart.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Traitement de l'ajout ou de la suppression d'articles
        Cart updatedCart = cartService.addItemToCart(cartId, articleId, quantity);

        // Vérification finale de la mise à jour du panier
        System.out.println("Cart mis à jour avec succès: " + updatedCart);

        return ResponseEntity.ok(updatedCart);
    }



    // Get cart by ID
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Integer cartId) { // Use Integer here
        Cart cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(cart);
    }

    // Get all carts
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @DeleteMapping("/{cartId}/items/{articleId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Integer cartId, @PathVariable Integer articleId) {
        Cart cart = cartService.removeItemFromCart(cartId, articleId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable Integer cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<Cart> getMyCart(@CookieValue(value = "token", defaultValue = "") String token) {
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Integer userId;
        try {
            // Utilisez le token directement car il est extrait du cookie
            userId = jwtTokenManager.getUserIdFromToken(token);
            System.out.println("ID utilisateur extrait du JWT : " + userId);
        } catch (Exception e) {
            // En cas d'erreur, retournez une réponse 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Récupérez le panier pour l'utilisateur extrait
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            System.out.println("Aucun panier trouvé pour l'utilisateur : " + userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cart);
    }



}
