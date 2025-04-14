package org.wildcodeschool.myblog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleCreateDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.service.ArticleService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleCreateDTO articleCreateDTO) {
        ArticleDTO savedArticle = articleService.createArticle(articleCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        ArticleDTO updateArticle = articleService.updateArticle(id, articleDetails);
        if (updateArticle == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updateArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Long id) {
       if (articleService.deleteArticle(id)) {
           return ResponseEntity.noContent().build();
       } else {
           return ResponseEntity.notFound().build();
       }
    }

    @GetMapping("/search-content")
    public ResponseEntity<List<ArticleDTO>> getArticlesByContent(@RequestParam String content) {
        List<ArticleDTO> articles = articleService.getArticlesByContent(content);
        if (articles == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search-created")
    public ResponseEntity<List<ArticleDTO>> getArticlesCreateAfter(@RequestParam LocalDateTime date) {
        List<ArticleDTO> articles = articleService.getArticlesCreateAfter(date);
        if (articles == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/top5")
    public ResponseEntity<List<ArticleDTO>> getFiveLastArticles() {
        List<ArticleDTO> articles = articleService.getFiveLastArticles();
        if (articles == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(articles);
    }
}
