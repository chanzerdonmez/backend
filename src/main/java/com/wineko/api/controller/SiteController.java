package com.wineko.api.controller;

import com.wineko.api.model.Article;
import com.wineko.api.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
// @RequestMapping("/ws/articles")
@RestController // json
public class SiteController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/product/all")
    public List<Article> allArticles(){
        return this.articleService.getAll();
    }

    @DeleteMapping("/product/{id}")
    public Article deleteArticle(@PathVariable Integer id){
        Article article = this.articleService.byId(id);
        article.setActive(!article.isActive());
        this.articleService.save(article);
        return article;
    }

    @PostMapping("/")
    public Article recupFormArticle(@ModelAttribute Article article){
        this.articleService.save(article);
        return article;
    }


}
