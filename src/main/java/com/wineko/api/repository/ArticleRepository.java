package com.wineko.api.repository;

import com.wineko.api.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
