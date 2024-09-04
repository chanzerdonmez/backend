package com.wineko.api.service;

import com.wineko.api.model.*;
import com.wineko.api.repository.ArticleRepository;
import com.wineko.api.repository.CartRepository;
import com.wineko.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wineko.api.model.CartStatus;


import java.util.Date;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);


    public Cart createCart(Integer userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedDate(new Date());
//      cart.setStatus(CartStatus.NEW);
        return cartRepository.save(cart); // Utilisation de save() pour créer un nouveau panier
    }

    public Cart addItemToCart(Integer cartId, Integer articleId, Integer quantity) {
        logger.debug("Start addItemToCart: cartId={}, articleId={}, quantity={}", cartId, articleId, quantity);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));
        logger.debug("Cart found: {}", cart);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));
        logger.debug("Article found: {}", article);

        CartLine existingLine = cart.getCartLines().stream()
                .filter(line -> line.getArticle().getId().equals(articleId))
                .findFirst()
                .orElse(null);

        if (existingLine != null) {
            int newQuantity = existingLine.getQuantity() + quantity;
            if (newQuantity > 0) {
                existingLine.setQuantity(newQuantity);
                existingLine.setPrice(article.getPrice() * newQuantity);
                logger.debug("Updated existing cart line: {}", existingLine);
            } else {
                // Si la quantité devient 0 ou négative, retirez la ligne du panier
                cart.getCartLines().remove(existingLine);
                logger.debug("Removed cart line: {}", existingLine);
            }
        } else if (quantity > 0) {
            // Ajouter une nouvelle ligne seulement si la quantité est positive
            CartLine newLine = new CartLine();
            newLine.setArticle(article);
            newLine.setQuantity(quantity);
            newLine.setPrice(article.getPrice() * quantity);
            newLine.setCart(cart);
            cart.getCartLines().add(newLine);
            logger.debug("Added new cart line: {}", newLine);
        } else {
            logger.warn("Attempted to add a new cart line with non-positive quantity");
        }

        Cart updatedCart = cartRepository.save(cart);
        logger.debug("Cart saved: {}", updatedCart);

        return updatedCart;
    }




    public Cart getCartById(Integer cartId) { // Use Integer here
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
    }


    public List<Cart> getAllCarts()

    {
        return cartRepository.findAll();
    }

    public Cart removeItemFromCart(Integer cartId, Integer articleId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Panier non trouvé"));
        cart.getCartLines().removeIf(line -> line.getArticle().getId().equals(articleId));
        return cartRepository.save(cart);
    }

    public Cart updateItemQuantity(Integer cartId, Integer articleId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Panier non trouvé"));
        for (CartLine line : cart.getCartLines()) {
            if (line.getArticle().getId().equals(articleId)) {
                line.setQuantity(quantity);
                break;
            }
        }
        return cartRepository.save(cart);
    }

    public void clearCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));
        cart.getCartLines().clear(); // Vide les lignes de commande du panier
        cartRepository.save(cart); // Sauvegarde le panier vidé dans la base de données
    }

    public class CartNotFoundException extends RuntimeException {
        public CartNotFoundException(Integer cartId) {
            super("Cart with ID " + cartId + " not found");


        }
    }


    public Cart getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Aucun panier trouvé pour l'utilisateur ID: " + userId));
    }


}


