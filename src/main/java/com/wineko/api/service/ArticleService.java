package com.wineko.api.service;
import com.wineko.api.model.Article;
import com.wineko.api.repository.ArticleRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Retourne tous les articles
     * 
     * @return list<Article>
     */
    public List<Article> getAll(){
        
        List<Article> articles;
        try {
            articles =  this.articleRepository.findAll();
        } catch(Exception exception) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY); 
        }
        return articles;
    }

    /**
     * retourne la liste des articles activés
     * 
     * @return List<Article>
     */
    public List<Article> getActiveArticleAll(){
        
        List<Article> articles;
        try {
            articles =  this.articleRepository.findAll();
        } catch(Exception exception) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY); 
        }
        return articles.stream()
                // on filtre les articles actifs
                .filter(Article::isActive)
                // on récupère la liste finale
                .collect(Collectors.toList());
    }

    @Cacheable
    public Page<Article> getActiveArticlePaginateAll(int page, int size){
        
        List<Article> articles;
        try {
            articles =  this.articleRepository.findAll();
        } catch(Exception exception) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY); 
        }
        List<Article> activeArticles =  articles.stream()
                // on filtre les articles actifs
                .filter(Article::isActive)
                // on récupère la liste finale
                .collect(Collectors.toList());

        int start = (page-1) * size;
        int end = Math.min(start + size, activeArticles.size());

        List<Article> pageContent = activeArticles.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), activeArticles.size());
    }



    /**
     * Récupérer l'ensemble des biens
     * @return Article
     */
    public Article getById(Integer id){
        return this.articleRepository.findById(id).orElseThrow(()-> new EntityNotFoundException ("Product not found"));
    }

    /**
     * Méthode qui permet de faire l'ajout et la modification<br>
     * - l'ajout : id = null <br>
     * - modification : id != null
     * @param article
     * @return
     */
    public Article save(Article article){
        try {
            this.articleRepository.save(article);
        } catch(Exception e) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY); 
        }
        return this.articleRepository.save(article);
    }

    /**
     * Methode permettant de modifier un article
     * 
     * @param id
     * @param updated
     * @return
     */
    public Article update(Integer id, Article updated){
        updated.setId(id);
        return this.articleRepository.save(updated);
    }

    /**
     * Passe le champ 'active' de l'article à false
     * 
     * @param id
     * @return
     */
    public String deleteArticle(Integer id) {
        Article article = this.getById(id);
        article.setActive(!article.isActive());
        this.save(article);
        return "Record successfully deleted"; 
    }

    /**
     * Supprime un article de la base de données
     * 
     * @param id
     * @return
     */
    public String deepDeleteArticle(Integer id) {
        Article article = this.getById(id);
        this.articleRepository.delete(article);
        return "Record fully deleted"; 
    }

}
