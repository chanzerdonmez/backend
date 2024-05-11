package com.wineko.api.repository;

import com.wineko.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
