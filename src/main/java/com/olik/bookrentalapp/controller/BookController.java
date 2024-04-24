package com.olik.bookrentalapp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olik.bookrentalapp.models.Book;
import com.olik.bookrentalapp.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	@Autowired
	private BookService bookService;

	@GetMapping()
	public ResponseEntity<?> getAllBooks() {
		List<Book> books = bookService.getAllBooks();
		if(books.isEmpty()!=true) {
			return ResponseEntity.ok(books);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve books!" );
		}
	}

	@GetMapping("/by-id")
	public ResponseEntity<?> getBookById(@RequestParam Long id) {
		Book book = bookService.getBookById(id);
		if (book != null) {
			return ResponseEntity.ok(book);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve books with id: " + id );
		}        
	}

	@GetMapping("/by-author")
	public ResponseEntity<?>getBookByAuthorName(@RequestParam String name){
		List<Book> books = bookService.getBookByAuthor(name);
		if(books.isEmpty()!=true) {
			return ResponseEntity.ok(books);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve books with author: " + name );
		}
	}

	@GetMapping("/available-for-rent")
	public ResponseEntity<?>getAllBooksAvailableForRent(){
		List<Book> books = bookService.getAllBooksAvailableForRent();
		if(books.isEmpty()!=true) {
			return ResponseEntity.ok(books);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve books available for rent");
		}
	}

	@GetMapping("/currently-rented")
	public ResponseEntity<?> getAllBooksCurrentlyRented(){
		List<Book>books = bookService.getAllBooksCurrentlyRented();
		if(books.isEmpty()!=true) {
			return ResponseEntity.ok(books);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve books currently rented!");
		}
	}
	@PostMapping("/{id}")
	public ResponseEntity<?> addBook(@RequestBody Book book, @PathVariable Long id) {
		if(book == null || id == null) {
			return ResponseEntity.badRequest().body("Book details and author id are required!");
		}
		Book savedBook = bookService.addBook(book, id);
		if(savedBook!=null) {
			return ResponseEntity.created(URI.create("/api/books/" + savedBook.getId())).body(savedBook);
		}else {
			return ResponseEntity.status(500).body("Failed to add book with this author id: " + id
					+ "( Possible reason: No author present with such author id!)");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book updatedBook, @RequestParam Long author_id) {
		Book book = bookService.updateBook(id, updatedBook, author_id);
		if (book != null) {
			return ResponseEntity.ok(book);
		} else {
			return ResponseEntity.status(500).body("Failed to update book with id: "+ id+ " or: " + author_id
					+ "( Possible reason: No book present with such id or no author present with such author id!)");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable Long id) {
		boolean deleted = bookService.deleteBook(id);
		if (deleted) {
			return ResponseEntity.ok("Book has been deleted successfully!");
		} else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with ID " + id + " not found");
	    }
	}




}
