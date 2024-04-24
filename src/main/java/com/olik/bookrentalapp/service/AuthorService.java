package com.olik.bookrentalapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olik.bookrentalapp.models.Author;
import com.olik.bookrentalapp.repository.AuthorRepository;

@Service
public class AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	
	public List<Author> getAllAuthors(){
		return authorRepository.findAll();
	}
	public Author getAuthorById(Long id) {
		Optional<Author>authorOptional = authorRepository.findById(id);
		return authorOptional.orElse(null);
	}
	public Author addAuthor(Author author) {
		return authorRepository.save(author);
	}
	public Author updateAuthor(Long id, Author updatedAuthor) {
		Optional<Author>existingAuthorOptional = authorRepository.findById(id);
		if(existingAuthorOptional.isPresent()) {
			Author existingAuthor = existingAuthorOptional.get();
			existingAuthor.setName(updatedAuthor.getName());
			existingAuthor.setBiography(updatedAuthor.getBiography());
			return authorRepository.save(existingAuthor);
		}else {
			return null;
		}
	}
	public boolean deleteAuthor(Long id) {
		if(authorRepository.existsById(id)) {
			authorRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}
}
