package com.wineko.api.service;

import com.wineko.api.model.Article;
import com.wineko.api.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAll(){
        return this.articleRepository.findAll();
    }

    /**
     * Méthode qui permet de faire l'ajout et la modification<br>
     * - l'ajout : id = null <br>
     * - modification : id != null
     * @param article
     * @return
     */
    public Article save(Article article){
        return this.articleRepository.save(article);
    }

    /**
     * Récupérer l'ensemble des biens
     * @return
     */

    public Article byId(Integer id){
        return this.articleRepository.findById(id).orElse(new Article());
    }

}
