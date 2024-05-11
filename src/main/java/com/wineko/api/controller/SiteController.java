package com.wineko.api.controller;
import com.wineko.api.model.Article;
import com.wineko.api.service.ArticleService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class SiteController {
    private ArticleService articleService;

    public SiteController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/article/get/all")
    public List<Article> allArticles(){
        return this.articleService.getAll();
    }

    @GetMapping("/article/get/active/all")
    public List<Article> getActiveArticleAll(){
        return this.articleService.getActiveArticleAll();
    }

    @GetMapping("/article/get/active/all/paginated/{page}/{size}")
    public Page<Article> getActiveArticlePaginateAll(@PathVariable Integer page, @PathVariable Integer size ){
        return this.articleService.getActiveArticlePaginateAll(page, size);
    }

    @GetMapping("/article/get/{id}")
    public Article articleById(@PathVariable Integer id){
        return this.articleService.getById(id);
    }

    @GetMapping("/article/delete/{id}")
    public String deleteArticle(@PathVariable Integer id){
        return this.articleService.deleteArticle(id);  
    }

    @GetMapping("/article/delete/deep/{id}")
    public String deepDeleteArticle(@PathVariable Integer id){
        return this.articleService.deepDeleteArticle(id);  
    }

    @PostMapping("/article/save")
    public Article saveArticle(@RequestBody Article article){
        this.articleService.save(article);
        return article;
    }

    @PostMapping("/article/update/{id}")
    public Article updateArticle(
        @PathVariable Integer id, 
        @RequestBody Article article
    ){
        this.articleService.update(id, article);
        return article;
    }

    @GetMapping("/")
    public String respond(){
        return "Welcome on my project backend!! :)";
    }


}
