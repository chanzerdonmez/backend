package com.wineko.api.repository;

import com.wineko.api.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByEmailLikeIgnoreCase(String email);

    boolean existsByToken(String token);

    Users findByToken(String token);

    Users findByEmail(String email);

}
