package com.wineko.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.wineko.api.model.Article;
import com.wineko.api.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById_Success() {
        // Given
        Integer articleId = 1;
        Article article = new Article();
        article.setId(articleId);

        // When
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        Article foundArticle = articleService.getById(articleId);

        // Then
        assertEquals(articleId, foundArticle.getId());
    }

    @Test
    public void testGetById_NotFound() {
        // Given
        Integer articleId = 1;

        // When
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            articleService.getById(articleId);
        });
    }
}
