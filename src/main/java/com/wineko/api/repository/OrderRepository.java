package com.wineko.api.repository;

import com.wineko.api.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface OrderRepository extends JpaRepository<Orders, Integer> {

    @Query("SELECT o FROM Orders o JOIN FETCH o.users")
    List<Orders> findAllWithUsers();

    List<Orders> findByUsersId(Integer userId);


}
