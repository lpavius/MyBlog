package org.wildcodeschool.myblog.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastname;

    @OneToMany(mappedBy = "author")
    private List<ArticleAuthor> articlesAuthor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public List<ArticleAuthor> getArticlesAuthor() {
        return articlesAuthor;
    }

    public void setArticlesAuthor(List<ArticleAuthor> articlesAuthor) {
        this.articlesAuthor = articlesAuthor;
    }
}
