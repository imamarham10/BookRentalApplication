package com.olik.bookrentalapp.models;

import java.time.Year;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String title;
	@ManyToOne
	private Author author;
	@Column(nullable = false, unique = true)
	private String isbn;
	@Column(nullable = false)
	private Year publicationYear;
	private boolean isAvailableForRent = true;
	public boolean isAvailableForRent() {
		return isAvailableForRent;
	}

	public void setAvailableForRent(boolean isAvailableForRent) {
		this.isAvailableForRent = isAvailableForRent;
	}

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
	private List<Rental> rentals;
	public Book() {
		// TODO Auto-generated constructor stub
	}

	public Book(Long id, String title, Author author, String isbn, Year publicationYear) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.publicationYear = publicationYear;
	}

	public List<Rental> getRentals() {
		return rentals;
	}

	public void setRentals(List<Rental> rentals) {
		this.rentals = rentals;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Year getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Year publicationYear) {
		this.publicationYear = publicationYear;
	}
}
