package org.wildcodeschool.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.ArticleAuthorDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Author;

import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public AuthorDTO convertDTO(Author author) {
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
}
