package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;

public class ArticleAuthorDTO {

    private Long id;
    @NotBlank(message = "Le nom de l'auteur ne doit pas être vide")
    private String authorName;
    @NotBlank(message = "Le nom de l'article ne doit pas être vide")
    private String articleName;
    @NotBlank(message = "La contribution de l'auteur ne doit pas être vide")
    private String contribution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
}
