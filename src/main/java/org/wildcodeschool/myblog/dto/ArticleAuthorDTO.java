package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Author;

public class ArticleAuthorDTO {

    private Long id;
    private String authorName;
    private String articleName;
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
