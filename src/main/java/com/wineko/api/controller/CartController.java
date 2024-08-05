package com.wineko.api.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private static final String CART_SESSION_KEY = "cart";

    // Ajouter un article au panier
    @PostMapping("/add/{articleId}")
    public Map<String, String> addToCart(@PathVariable Integer articleId, HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(articleId, cart.getOrDefault(articleId, 0) + 1);
        session.setAttribute(CART_SESSION_KEY, cart);

        // Log pour vérifier l'état du panier
        System.out.println("Panier actuel après ajout: " + cart);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Article ajouté au panier");
        return response;
    }

    // Récupérer le contenu du panier
    @GetMapping
    public Map<Integer, Integer> getCart(HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            System.out.println("Panier est vide ou n'a pas encore été initialisé.");
            return new HashMap<>(); // Retourne un panier vide si rien n'est trouvé
        }
        System.out.println("Récupération du panier: " + cart);
        return cart;
    }

    // Supprimer un article du panier
    @DeleteMapping("/remove/{articleId}")
    public String removeFromCart(@PathVariable Integer articleId, HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute(CART_SESSION_KEY);
        if (cart != null) {
            cart.remove(articleId);
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return "Article retiré du panier";
    }

    // Vider le panier
    @DeleteMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
        return "Panier vidé";
    }
}
