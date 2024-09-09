package com.wineko.api.controller;

import com.wineko.api.model.Article;
import com.wineko.api.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/article")
@CrossOrigin(origins = {"http://localhost:4200", "https://wineko.srv589783.hstgr.cloud"})
public class ArticleController {

    @Value("${file.upload-dir}")
    private String uploadDir;

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


    @PostMapping("/upload/{id}")
    public String uploadImage(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        return this.articleService.uploadImage(id, file);
    }




    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            System.out.println("Fetching image from: " + filePath.toAbsolutePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                System.out.println("Image not found or unreadable");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Exception while fetching image: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }




}
