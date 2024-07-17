package com.wineko.api.controller;

import com.wineko.api.model.Category;
import com.wineko.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get/all")
    public List<Category> allCategories(){
        return this.categoryService.getAll();
    }

    @GetMapping("/get/active/all")
    public List<Category> getActiveCategoryAll(){
        return this.categoryService.getActiveCategoryAll();
    }

    @GetMapping("/get/active/all/paginated/{page}/{size}")
    public Page<Category> getActiveCategoryPaginateAll(@PathVariable Integer page, @PathVariable Integer size ){
        return this.categoryService.getActiveCategoryPaginateAll(page, size);
    }

    @GetMapping("/get/{id}")
    public Category categoryById(@PathVariable Integer id){
        return this.categoryService.getById(id);
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Integer id){
        return this.categoryService.deleteCategory(id);
    }

    @GetMapping("/delete/deep/{id}")
    public String deepDeleteArticle(@PathVariable Integer id){
        return this.categoryService.deepDeleteCategory(id);
    }

    @PostMapping("/save")
    public Category saveCategory(@RequestBody Category category){
        category = this.categoryService.save(category);
        return category;
    }

    @GetMapping("/save")
    public List<Category> getCategory(){
        return new ArrayList<>();
    }

    @PatchMapping("/update/{id}")
    public Category updateCategory(
            @PathVariable Integer id,
            @RequestBody Category category
    ){
        this.categoryService.update(id, category);
        return category;
    }

}