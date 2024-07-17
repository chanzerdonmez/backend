package com.wineko.api.controller;

import com.wineko.api.model.Article;
import com.wineko.api.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/article")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/get/all")
    public List<Article> allArticles(){
        return this.articleService.getAll();
    }

    @GetMapping("/get/active/all")
    public List<Article> getActiveArticleAll(){
        return this.articleService.getActiveArticleAll();
    }

    @GetMapping("/get/active/all/paginated/{page}/{size}")
    public Page<Article> getActiveArticlePaginateAll(@PathVariable Integer page, @PathVariable Integer size ){
        return this.articleService.getActiveArticlePaginateAll(page, size);
    }

    @GetMapping("/get/{id}")
    public Article articleById(@PathVariable Integer id){
        return this.articleService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Integer id){
        return this.articleService.deleteArticle(id);
    }

    @DeleteMapping("/delete/deep/{id}")
    public String deepDeleteArticle(@PathVariable Integer id){
        return this.articleService.deepDeleteArticle(id);
    }

    @PostMapping("/save")
    public Article saveArticle(@RequestBody Article article){
        article = this.articleService.save(article);
        return article;
    }

    @GetMapping("/save")
    public List<Article> getArticle(){
        return new ArrayList<>();
    }

    @PatchMapping("/update/{id}")
    public Article updateArticle(
            @PathVariable Integer id,
            @RequestBody Article article
    ){
        this.articleService.update(id, article);
        return article;
    }

    @GetMapping("/get/category/{categoryTitle}")
    public List<Article> getArticlesByCategory(@PathVariable String categoryTitle) {
        return this.articleService.getArticlesByCategory(categoryTitle);
    }

    @GetMapping("/get/recent")
    public List<Article> getRecentArticles() {
        return this.articleService.getRecentArticles();
    }
}
