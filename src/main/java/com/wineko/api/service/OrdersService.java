package com.wineko.api.service;

import com.wineko.api.model.Orders;
import com.wineko.api.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class OrdersService {

    private final OrderRepository orderRepository;

//    @Autowired
    public OrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

//    public List<Orders> getAll(){
//        List<Orders> orders;
//        try {
//
//            orders = this.orderRepository.findAll();
//System.out.println("string test"+ orders);        } catch(Exception exception) {
//
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unable to fetch orders", exception);
//        }
//        return orders;
//    }

    public List<Orders> getAll() {
        return orderRepository.findAllWithUsers();
    }
}
