package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public AuthorDTO createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertDTO(savedAuthor);
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            return null;
        }
        return authors.stream().map(authorMapper::convertDTO).toList();
    }

    public AuthorDTO getAuthorById(@PathVariable Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l'auteur avec l'id " + id + " n'a pas été trouvé"));
        if(author == null)
            return null;
        return authorMapper.convertDTO(author);
    }

    public AuthorDTO updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l'auteur avec l'id " + id + " n'a pas été trouvé"));
        if (author == null)
            return null;
        author.setFirstname(authorDetails.getFirstname());
        author.setLastname(authorDetails.getLastname());
        Author authorUpdated = authorRepository.save(author);
        return authorMapper.convertDTO(authorUpdated);
    }

    public boolean deleteAuthor(@PathVariable Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l'auteur avec l'id " + id + " n'a pas été trouvé"));
        if (author == null)
            return false;
        authorRepository.delete(author);
        return true;
    }
}
