package com.olik.bookrentalapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olik.bookrentalapp.models.Author;
import com.olik.bookrentalapp.models.Book;
import com.olik.bookrentalapp.models.Rental;
import com.olik.bookrentalapp.repository.AuthorRepository;
import com.olik.bookrentalapp.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getBookById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		return book.orElse(null);
	}

	public Book addBook(Book book, long author_id) {
		Optional<Author> dbAuthor = authorRepository.findById(author_id);
		if (dbAuthor.isPresent()) {
			book.setAuthor(dbAuthor.get());
			return bookRepository.save(book);
		}
		return null;
	}

	public Book updateBook(Long id, Book updatedBook, Long author_id) {
		Optional<Book> bookOptional = bookRepository.findById(id);
		Optional<Author> authorOptional = authorRepository.findById(id);
		if (bookOptional.isPresent() && authorOptional.isPresent()) {
			Book existingBook = bookOptional.get();
			existingBook.setTitle(updatedBook.getTitle());
			existingBook.setAuthor(authorOptional.get());
			existingBook.setIsbn(updatedBook.getIsbn());
			existingBook.setPublicationYear(updatedBook.getPublicationYear());
			return bookRepository.save(existingBook);
		} else {
			return null;
		}
	}

	public boolean deleteBook(Long id) {
		if (bookRepository.existsById(id)) {
			bookRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	public List<Book> getBookByAuthor(String authorName) {
		return bookRepository.getBookByAuthor(authorName);
	}
	
	public List<Book> getAllBooksAvailableForRent(){
		return bookRepository.findBooksByAvailableForRent();
	}
	
	public List<Book> getAllBooksCurrentlyRented(){
		 List<Book> books = bookRepository.findAll();
		    List<Book> currentlyRentedBooks = new ArrayList<>();
		    for (Book book : books) {
		        if (!book.isAvailableForRent()) {
		            currentlyRentedBooks.add(book);
		        }
		    }
		    return currentlyRentedBooks;
	}
	
	//public void 
}
