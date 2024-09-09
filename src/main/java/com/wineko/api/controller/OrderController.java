package com.wineko.api.controller;

import com.wineko.api.dto.OrderDto;
import com.wineko.api.manager.JwtTokenManager;
import com.wineko.api.mapper.OrderMapper;
import com.wineko.api.model.Address;
import com.wineko.api.model.OrderLine;
import com.wineko.api.model.Orders;
import com.wineko.api.service.AddressService;
import com.wineko.api.service.OrdersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "https://chanzerdonmez.github.io/sitewineko")
public class OrderController {

    private final OrdersService ordersService;
    private final JwtTokenManager jwtTokenManager;

    private final OrderMapper orderMapper;


    // Un Map temporaire pour stocker les commandes par utilisateur
    private final Map<Integer, Map<String, Object>> temporaryOrderStorage = new HashMap<>();

    @Autowired
    public OrderController(OrdersService ordersService, JwtTokenManager jwtTokenManager, OrderMapper orderMapper) {
        this.ordersService = ordersService;
        this.jwtTokenManager = jwtTokenManager;
        this.orderMapper = orderMapper;

    }

    @Autowired
    private AddressService addressService;


    @GetMapping("/get/all")
    public ResponseEntity<List<OrderDto>> allOrders() {
        List<Orders> orders = ordersService.getAll();
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }


    @PostMapping("/initialize")
    public ResponseEntity<Map<String, Object>> initializeOrder(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        // Récupérer le JWT à partir du cookie "token"
        String token = getJwtFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extraire l'ID utilisateur à partir du JWT
        Integer userId = jwtTokenManager.getUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Stocker les données de la commande avec l'ID de l'utilisateur
        temporaryOrderStorage.put(userId, payload);

        return ResponseEntity.ok(payload); // Retourner les données pour confirmation avant paiement
    }

    @PostMapping("/finalize")
    public ResponseEntity<Orders> finalizeOrder(HttpServletRequest request) {
        String token = getJwtFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Integer userId = jwtTokenManager.getUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> payload = temporaryOrderStorage.get(userId);
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Orders order = new Orders();
        order.setNumberOrder((String) payload.get("numberOrder"));
        order.setDateCreation(new Date());

        // Création des adresses
        Address billingAddress = new Address();
        billingAddress.setName((String) payload.get("billingStreetName"));
        billingAddress.setFirstName((String) payload.get("billingStreetName"));
        billingAddress.setStreetName((String) payload.get("billingStreetName"));
        billingAddress.setStreetNumber((String) payload.get("billingStreetNumber"));
        billingAddress.setCity((String) payload.get("billingCity"));
        billingAddress.setZipCode((String) payload.get("billingZipCode"));

        Address shippingAddress = new Address();
        shippingAddress.setName((String) payload.get("shippingStreetName"));
        shippingAddress.setFirstName((String) payload.get("shippingStreetName"));
        shippingAddress.setStreetName((String) payload.get("shippingStreetName"));
        shippingAddress.setStreetNumber((String) payload.get("shippingStreetNumber"));
        shippingAddress.setCity((String) payload.get("shippingCity"));
        shippingAddress.setZipCode((String) payload.get("shippingZipCode"));

        // Sauvegarder les adresses avant de les associer à l'ordre
        billingAddress = addressService.saveAddress(billingAddress);
        shippingAddress = addressService.saveAddress(shippingAddress);

        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);

        List<Map<String, Object>> orderLinesMap = (List<Map<String, Object>>) payload.get("orderLines");
        List<OrderLine> orderLines = orderLinesMap.stream().map(lineMap -> {
            OrderLine line = new OrderLine();
            line.setProductName((String) lineMap.get("productName"));
            line.setQuantity((Integer) lineMap.get("quantity"));

            Object unitPriceObj = lineMap.get("unitPrice");
            Double unitPrice = (unitPriceObj instanceof Integer) ? ((Integer) unitPriceObj).doubleValue() : (Double) unitPriceObj;
            line.setUnitPrice(unitPrice);

            return line;
        }).toList();

        order.setUsers(ordersService.getUserById(userId));

        Orders createdOrder = ordersService.createOrder(order, orderLines);
        temporaryOrderStorage.remove(userId);

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/get/user-orders")
    public ResponseEntity<List<OrderDto>> getUserOrders(HttpServletRequest request) {
        String token = getJwtFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Integer userId = jwtTokenManager.getUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Orders> userOrders = ordersService.getOrdersByUserId(userId);
        List<OrderDto> orderDtos = userOrders.stream().map(orderMapper::toDto).toList();

        return ResponseEntity.ok(orderDtos);
    }



    private String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
