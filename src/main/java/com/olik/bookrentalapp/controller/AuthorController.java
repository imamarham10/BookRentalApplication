package com.olik.bookrentalapp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olik.bookrentalapp.models.Author;
import com.olik.bookrentalapp.service.AuthorService;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
	@Autowired
	private AuthorService authorService;
	@PostMapping
	public ResponseEntity<?> addBook(@RequestBody Author author) {
		if(author==null) {
			return ResponseEntity.badRequest().body("Author details are required!");
		}
		Author savedAuthor = authorService.addAuthor(author);
		if(savedAuthor!=null) {
			return ResponseEntity.created(URI.create("/api/books/" + savedAuthor.getId())).body(savedAuthor);
		}else {
			return ResponseEntity.status(500).body("Failed to add author "
					+ "( Possible reason: Author details might not be entered correctly!)");
		}
	}
	
	@GetMapping
	public ResponseEntity<?> getAllAuthors(){
		List<Author> authors = authorService.getAllAuthors();
		if(authors!=null) {
			return ResponseEntity.ok(authors);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve authors!" );
		}
	}
	
	@GetMapping("/by-id")
	public ResponseEntity<?> getAuthorById(@RequestParam Long author_id){
		Author author = authorService.getAuthorById(author_id);
		if(author!=null) {
			return ResponseEntity.ok(author);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve book with id: " + author_id );
		}
	}
	
	@PutMapping
	public ResponseEntity<?> updateAuthor(@RequestParam Long author_id, @RequestBody Author author){
		Author updatedAuthor = authorService.updateAuthor(author_id, author);
		if(updatedAuthor!=null) {
			return ResponseEntity.ok(updatedAuthor);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update author with id: " + author_id );
		}
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteAuthor(@RequestParam Long author_id){
		boolean authorDelete = authorService.deleteAuthor(author_id);
		if(authorDelete==true) {
			return ResponseEntity.ok("Author deleted with id: " + author_id);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author failed to delete with id: " + author_id );
		}
	}
}
