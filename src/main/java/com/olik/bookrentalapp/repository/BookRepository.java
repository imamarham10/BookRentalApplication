package com.olik.bookrentalapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olik.bookrentalapp.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	@Query(value = "select b from Book b where b.author.name=?1")
	List<Book> getBookByAuthor(String authorName);
	
	@Query(value = "select b from Book b where b.isAvailableForRent=true")
	List<Book> findBooksByAvailableForRent();
	
	
}
