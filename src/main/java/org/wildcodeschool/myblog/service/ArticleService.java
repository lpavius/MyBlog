package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.mapper.ArticleMapper;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    public ArticleService(ArticleRepository articleRepository,
                          ArticleMapper articleMapper,
                          CategoryRepository categoryRepository,
                          ImageRepository imageRepository,
                          AuthorRepository authorRepository,
                          ArticleAuthorRepository articleAuthorRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(@PathVariable Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }
        return articleMapper.convertToDTO(article);
    }

    public ArticleDTO createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            article.setCategory(category);
        }
        if (article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null ) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null;
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }
        Article savedArticle = articleRepository.save(article);
        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                Author author = articleAuthor.getAuthor();
                author = authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return null;
                }
                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(articleAuthor.getContribution());
                articleAuthorRepository.save(articleAuthor);
            }
        }
        return articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());
        if (articleDetails.getCategory() != null) {
            Category category = categoryRepository.findById(articleDetails.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            article.setCategory(category);
        }
        if (articleDetails.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : articleDetails.getImages()) {
                if (image.getId() != null) {
                    // Vérification des images existantes
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null; // Image non trouvée, retour d'une erreur
                    }
                } else {
                    // Création de nouvelles images
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            // Mettre à jour la liste des images associées
            article.setImages(validImages);
        } else {
            // Si aucune image n'est fournie, on nettoie la liste des images associées
            article.getImages().clear();
        }
        if (articleDetails.getArticleAuthors() != null) {
            // Supprimer manuellement les anciens ArticleAuthor
            for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
                Author author = articleAuthorDetails.getAuthor();
                author = authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return null;
                }

                // Créer et associer la nouvelle relation ArticleAuthor
                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);
            }

            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                articleAuthorRepository.save(articleAuthor);
            }

            article.setArticleAuthors(updatedArticleAuthors);
        }
        Article updateArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updateArticle);
    }

    public boolean deleteArticle(@PathVariable Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return false;
        }
        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
        return true;
    }

    public List<ArticleDTO> getArticlesByContent(@RequestParam String content) {
        List<Article> articles = articleRepository.findByContentContaining(content);
        if (articles == null) return null;
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());

    }

    public List<ArticleDTO> getArticlesCreateAfter(@RequestParam LocalDateTime date) {
        List<Article> articles = articleRepository.findAllByCreatedAtAfter(date);
        if (articles == null) return null;
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<ArticleDTO> getFiveLastArticles() {
        List<Article> articles = articleRepository.findTop5ByOrderByCreatedAtDesc();
        if (articles == null) return null;
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }
}
