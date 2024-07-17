package com.wineko.api.service;
import com.wineko.api.model.Article;
import com.wineko.api.model.Category;
import com.wineko.api.repository.ArticleRepository;
import com.wineko.api.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    @Autowired
    private  CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retourne tous les categories
     *
     * @return list<Category>
     */
    public List<Category> getAll(){

        List<Category> categories;
        try {
            categories =  this.categoryRepository.findAll();
        } catch(Exception exception) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return categories;
    }

    /**
     * retourne la liste des categories activés
     *
     * @return List<Category>
     */
    public List<Category> getActiveCategoryAll(){

        List<Category> categories;
        try {
            categories =  this.categoryRepository.findAll();
        } catch(Exception exception) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return categories.stream()
                // on filtre les articles actifs
                .filter(Category::isActive)
                // on récupère la liste finale
                .collect(Collectors.toList());
    }

    @Cacheable
    public Page<Category> getActiveCategoryPaginateAll(int page, int size){

        List<Category> categories;
        try {
            categories =  this.categoryRepository.findAll();
        } catch(Exception exception) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<Category> activeCategories =  categories.stream()
                // on filtre les categories actifs
                .filter(Category::isActive)
                // on récupère la liste finale
                .collect(Collectors.toList());

        int start = (page-1) * size;
        int end = Math.min(start + size, activeCategories.size());

        List<Category> pageContent = activeCategories.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), activeCategories.size());
    }


    /**
     * Récupérer l'ensemble des biens
     * @return Category
     */
    public Category getById(Integer id){
        return this.categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException ("Product not found"));
    }

    /**
     * Méthode qui permet de faire l'ajout et la modification<br>
     * - l'ajout : id = null <br>
     * - modification : id != null
     * @param category
     * @return Category
     */
    public Category save(Category category){
        return this.categoryRepository.save(category);
    }

    /**
     * Methode permettant de modifier un article
     *
     * @param id
     * @param updated
     * @return
     */
    public Category update(Integer id, Category updated){
        updated.setId(id);
        return this.categoryRepository.save(updated);
    }

    /**
     * Passe le champ 'active' de la catégorie à false
     *
     * @param id
     * @return
     */
    public String deleteCategory(Integer id) {
        Category category = this.getById(id);
        category.setActive(!category.isActive());
        this.save(category);
        return "Record successfully deleted";
    }

    /**
     * Supprime un catégorie de la base de données
     *
     * @param id
     * @return
     */
    public String deepDeleteCategory(Integer id) {
        Category category = this.getById(id);
        this.categoryRepository.delete(category);
        return "Record fully deleted";
    }

}
