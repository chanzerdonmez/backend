package com.wineko.api.service;

import com.wineko.api.model.OrderLine;
import com.wineko.api.model.Orders;
import com.wineko.api.model.Users;
import com.wineko.api.repository.OrderLineRepository;
import com.wineko.api.repository.OrderRepository;
import com.wineko.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class OrdersService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrdersService(OrderRepository orderRepository, OrderLineRepository orderLineRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Orders createOrder(Orders order, List<OrderLine> orderLines) {
        // Associer l'utilisateur à la commande
        Users user = userRepository.findById(order.getUsers().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        order.setUsers(user);

        // Enregistrer la commande
        Orders savedOrder = orderRepository.save(order);

        // Associer et enregistrer les lignes de commande
        for (OrderLine line : orderLines) {
            line.setOrders(savedOrder);
            orderLineRepository.save(line);
        }

        return savedOrder;
    }

    public List<Orders> getAll() {
        return orderRepository.findAllWithUsers();
    }

    public List<Orders> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUsersId(userId);
    }

    // Ajout de la méthode getUserById pour récupérer l'utilisateur par son ID
    public Users getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }



}
