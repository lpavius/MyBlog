package org.wildcodeschool.myblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleAuthorDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private AuthorDTO convertDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstname(author.getFirstname());
        authorDTO.setLastname(author.getLastname());
        if (author.getArticlesAuthor() != null) {
            authorDTO.setArticleAuthors(author.getArticlesAuthor().stream().map(articleAuthor -> {
                ArticleAuthorDTO articleAuthorDTO = new ArticleAuthorDTO();
                articleAuthorDTO.setId(articleAuthor.getId());
                articleAuthorDTO.setAuthorName(articleAuthor.getAuthor().getFirstname()
                        + " " + articleAuthor.getAuthor().getLastname());
                articleAuthorDTO.setArticleName(articleAuthor.getArticle().getTitle());
                articleAuthorDTO.setContribution(articleAuthor.getContribution());
                return articleAuthorDTO;
            }).collect(Collectors.toList()));
        }
        return authorDTO;
    }

    @PostMapping
    private ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.status(201).body(convertDTO(savedAuthor));
    }

    @GetMapping
    private ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
           return ResponseEntity.noContent().build();
        }
        List<AuthorDTO> authorDTOS = authors.stream().map(this::convertDTO).toList();
        return ResponseEntity.ok(authorDTOS);
    }

    @GetMapping("/{id}")
    private ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertDTO(author));
    }

    @PutMapping("/{id}")
    private ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null)
           return ResponseEntity.notFound().build();
        author.setFirstname(authorDetails.getFirstname());
        author.setLastname(authorDetails.getLastname());
        Author authorUpdated = authorRepository.save(author);
        return ResponseEntity.ok(convertDTO(authorUpdated));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<AuthorDTO> deleteAuthor(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null)
            return ResponseEntity.notFound().build();
        authorRepository.delete(author);
        return ResponseEntity.noContent().build();
    }
}
