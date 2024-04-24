package com.olik.bookrentalapp.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Year;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olik.bookrentalapp.models.Author;
import com.olik.bookrentalapp.models.Book;
import com.olik.bookrentalapp.service.AuthorService;
import com.olik.bookrentalapp.service.BookService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
	
	@MockBean
	private BookService bookService;
	@MockBean
	private AuthorService authorService;
	@Autowired
    private ObjectMapper objectMapper;
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	@DisplayName("Should Add Book When making POST request to endpoint - /api/books/")
	void shouldAddBook() throws Exception {
	    // Given
	    Author author = new Author();
	    author.setId(1L);
	    author.setName("John Doe");
	    author.setBiography("An acclaimed author with several best-selling novels.");

	    Book bookRequest = new Book();
	    bookRequest.setTitle("New Book");
	    bookRequest.setAuthor(author);
	    bookRequest.setIsbn("1234567890");
	    bookRequest.setPublicationYear(Year.of(2024));

	    Book savedBookResponse = new Book();
	    savedBookResponse.setId(1L);
	    savedBookResponse.setTitle("New Book");
	    savedBookResponse.setAuthor(author);
	    savedBookResponse.setIsbn("1234567890");
	    savedBookResponse.setPublicationYear(Year.of(2024));

	    // Mock the AuthorService to return the author when getById is called
	    Mockito.when(authorService.getAuthorById(1L)).thenReturn(author);

	    Mockito.when(bookService.addBook(any(Book.class), eq(1L))).thenReturn(savedBookResponse);

	    // When/Then
	    mockMvc.perform(MockMvcRequestBuilders.post("/api/books/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(bookRequest)))
	            .andExpect(status().isCreated());
	}

	
	@Test
	@DisplayName("Should Update Book When making PUT request to endpoint - /api/books/{id}")
	void shouldUpdateBook() throws Exception {
	    // Given
	    Author author = new Author();
	    author.setId(1L);
	    author.setName("John Doe");
	    author.setBiography("An acclaimed author with several best-selling novels.");

	    Book updatedBookRequest = new Book();
	    updatedBookRequest.setTitle("Updated Book");
	    updatedBookRequest.setAuthor(author);
	    updatedBookRequest.setIsbn("0987654321");
	    updatedBookRequest.setPublicationYear(Year.of(2025));

	    Book updatedBookResponse = new Book();
	    updatedBookResponse.setId(1L);
	    updatedBookResponse.setTitle("Updated Book");
	    updatedBookResponse.setAuthor(author);
	    updatedBookResponse.setIsbn("0987654321");
	    updatedBookResponse.setPublicationYear(Year.of(2025));

	    Mockito.when(bookService.updateBook(eq(1L), any(Book.class), eq(1L))).thenReturn(updatedBookResponse);

	    // When/Then
	    mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", 1L)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(updatedBookRequest))
	            .param("author_id", "1"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.id", Matchers.is(1)))
	            .andExpect(jsonPath("$.title", Matchers.is("Updated Book")))
	            .andExpect(jsonPath("$.author.id", Matchers.is(1)))
	            .andExpect(jsonPath("$.author.name", Matchers.is("John Doe")))
	            .andExpect(jsonPath("$.author.biography", Matchers.is("An acclaimed author with several best-selling novels.")))
	            .andExpect(jsonPath("$.isbn", Matchers.is("0987654321")))
	            .andExpect(jsonPath("$.publicationYear", Matchers.is("2025")));
	}

	
	
	@Test
	@DisplayName("Should List All Books When making GET request to endpoint - /api/books/")
	@WithMockUser(username = "test")
	void shouldListBooks() throws Exception {
	    Author author1 = new Author(1L, "John Doe", "An acclaimed author with several best-selling novels.");
	    Author author2 = new Author(2L, "Jane Smith", "An award-winning author known for her captivating storytelling.");

	    Book bookResponse1 = new Book(1L, "Book Title 1", author1, "1234567890", Year.of(2022));
	    Book bookResponse2 = new Book(2L, "Book Title 2", author2, "0987654321", Year.of(2023));

	    Mockito.when(bookService.getAllBooks()).thenReturn(asList(bookResponse1, bookResponse2));

	    mockMvc.perform(get("/api/books"))
	            .andExpect(status().is(200))
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.size()", Matchers.is(2)))
	            .andExpect(jsonPath("$[0].id", Matchers.is(1)))
	            .andExpect(jsonPath("$[0].title", Matchers.is("Book Title 1")))
	            .andExpect(jsonPath("$[0].author.id", Matchers.is(1)))
	            .andExpect(jsonPath("$[0].author.name", Matchers.is("John Doe")))
	            .andExpect(jsonPath("$[0].author.biography", Matchers.is("An acclaimed author with several best-selling novels.")))
	            .andExpect(jsonPath("$[0].isbn", Matchers.is("1234567890")))
	            .andExpect(jsonPath("$[0].publicationYear", Matchers.is("2022"))) // Note: Changed to string comparison
	            .andExpect(jsonPath("$[1].id", Matchers.is(2)))
	            .andExpect(jsonPath("$[1].title", Matchers.is("Book Title 2")))
	            .andExpect(jsonPath("$[1].author.id", Matchers.is(2)))
	            .andExpect(jsonPath("$[1].author.name", Matchers.is("Jane Smith")))
	            .andExpect(jsonPath("$[1].author.biography", Matchers.is("An award-winning author known for her captivating storytelling.")))
	            .andExpect(jsonPath("$[1].isbn", Matchers.is("0987654321")))
	            .andExpect(jsonPath("$[1].publicationYear", Matchers.is("2023"))); // Note: Changed to string comparison
	}
	
	
	@Test
	@DisplayName("Should Get Book by ID When making GET request to endpoint - /api/books/by-id")
	@WithMockUser(username = "test")
	void shouldGetBookById() throws Exception {
	    Author author = new Author(1L, "John Doe", "An acclaimed author with several best-selling novels.");
	    Book bookResponse = new Book(1L, "Book Title", author, "1234567890", Year.of(2022));

	    Mockito.when(bookService.getBookById(1L)).thenReturn(bookResponse);

	    mockMvc.perform(get("/api/books/by-id").param("id", "1"))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.id", Matchers.is(1)))
	            .andExpect(jsonPath("$.title", Matchers.is("Book Title")))
	            .andExpect(jsonPath("$.author.id", Matchers.is(1)))
	            .andExpect(jsonPath("$.author.name", Matchers.is("John Doe")))
	            .andExpect(jsonPath("$.author.biography", Matchers.is("An acclaimed author with several best-selling novels.")))
	            .andExpect(jsonPath("$.isbn", Matchers.is("1234567890")))
	            .andExpect(jsonPath("$.publicationYear", Matchers.is("2022"))); // Note: Integer comparison
	}

	@Test
	@DisplayName("Should Get Books by Author Name When making GET request to endpoint - /api/books/by-author")
	@WithMockUser(username = "test")
	void shouldGetBooksByAuthorName() throws Exception {
	    Author author = new Author(1L, "John Doe", "An acclaimed author with several best-selling novels.");
	    Book book1 = new Book(1L, "Book Title 1", author, "1234567890", Year.of(2022));
	    Book book2 = new Book(2L, "Book Title 2", author, "0987654321", Year.of(2023));

	    Mockito.when(bookService.getBookByAuthor("John")).thenReturn(asList(book1, book2));

	    mockMvc.perform(get("/api/books/by-author").param("name", "John"))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.size()", Matchers.is(2)))
	            .andExpect(jsonPath("$[0].id", Matchers.is(1)))
	            .andExpect(jsonPath("$[0].title", Matchers.is("Book Title 1")))
	            .andExpect(jsonPath("$[0].author.id", Matchers.is(1)))
	            .andExpect(jsonPath("$[0].author.name", Matchers.is("John Doe")))
	            .andExpect(jsonPath("$[0].author.biography", Matchers.is("An acclaimed author with several best-selling novels.")))
	            .andExpect(jsonPath("$[0].isbn", Matchers.is("1234567890")))
	            .andExpect(jsonPath("$[0].publicationYear", Matchers.is("2022"))) // Note: Integer comparison
	            .andExpect(jsonPath("$[1].id", Matchers.is(2)))
	            .andExpect(jsonPath("$[1].title", Matchers.is("Book Title 2")))
	            .andExpect(jsonPath("$[1].author.id", Matchers.is(1)))
	            .andExpect(jsonPath("$[1].author.name", Matchers.is("John Doe")))
	            .andExpect(jsonPath("$[1].author.biography", Matchers.is("An acclaimed author with several best-selling novels.")))
	            .andExpect(jsonPath("$[1].isbn", Matchers.is("0987654321")))
	            .andExpect(jsonPath("$[1].publicationYear", Matchers.is("2023"))); // Note: Integer comparison
	}
	
	
	@Test
	@DisplayName("Should Get All Books Available for Rent When making GET request to endpoint - /api/books/available-for-rent")
	@WithMockUser(username = "test")
	void shouldGetAllBooksAvailableForRent() throws Exception {
	    Author author1 = new Author(1L, "John Doe", "An acclaimed author with several best-selling novels.");
	    Author author2 = new Author(2L, "Jane Smith", "An award-winning author known for her captivating storytelling.");

	    Book book1 = new Book(1L, "Book Title 1", author1, "1234567890", Year.of(2022));
	    Book book2 = new Book(2L, "Book Title 2", author2, "0987654321", Year.of(2023));
	    Book book3 = new Book(3L, "Book Title 3", author1, "9876543210", Year.of(2021));

	    // Set availability for rent
	    book1.setAvailableForRent(true);
	    book2.setAvailableForRent(false); // Not available for rent
	    book3.setAvailableForRent(true);

	    Mockito.when(bookService.getAllBooksAvailableForRent()).thenReturn(asList(book1, book3));

	    mockMvc.perform(get("/api/books/available-for-rent"))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.size()", Matchers.is(2)))
	            .andExpect(jsonPath("$[0].id", Matchers.is(1)))
	            .andExpect(jsonPath("$[0].title", Matchers.is("Book Title 1")))
	            .andExpect(jsonPath("$[0].author.id", Matchers.is(1)))
	            .andExpect(jsonPath("$[0].author.name", Matchers.is("John Doe")))
	            .andExpect(jsonPath("$[0].author.biography", Matchers.is("An acclaimed author with several best-selling novels.")))
	            .andExpect(jsonPath("$[0].isbn", Matchers.is("1234567890")))
	            .andExpect(jsonPath("$[0].publicationYear", Matchers.is("2022")))
	            .andExpect(jsonPath("$[1].id", Matchers.is(3)))
	            .andExpect(jsonPath("$[1].title", Matchers.is("Book Title 3")))
	            .andExpect(jsonPath("$[1].author.id", Matchers.is(1)))
	            .andExpect(jsonPath("$[1].author.name", Matchers.is("John Doe")))
	            .andExpect(jsonPath("$[1].author.biography", Matchers.is("An acclaimed author with several best-selling novels.")))
	            .andExpect(jsonPath("$[1].isbn", Matchers.is("9876543210")))
	            .andExpect(jsonPath("$[1].publicationYear", Matchers.is("2021")));
	}
	
	
	@Test
	@DisplayName("Should Get All Books Currently Rented When making GET request to endpoint - /api/books/currently-rented")
	@WithMockUser(username = "test")
	void shouldGetAllBooksCurrentlyRented() throws Exception {
	    Author author1 = new Author(1L, "John Doe", "An acclaimed author with several best-selling novels.");
	    Author author2 = new Author(2L, "Jane Smith", "An award-winning author known for her captivating storytelling.");

	    Book book1 = new Book(1L, "Book Title 1", author1, "1234567890", Year.of(2022));
	    Book book2 = new Book(2L, "Book Title 2", author2, "0987654321", Year.of(2023));
	    Book book3 = new Book(3L, "Book Title 3", author1, "9876543210", Year.of(2021));

	    // Set availability for rent
	    book1.setAvailableForRent(true); // Not rented
	    book2.setAvailableForRent(false); // Rented
	    book3.setAvailableForRent(true); // Not rented

	    Mockito.when(bookService.getAllBooksCurrentlyRented()).thenReturn(asList(book2));

	    mockMvc.perform(get("/api/books/currently-rented"))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.size()", Matchers.is(1)))
	            .andExpect(jsonPath("$[0].id", Matchers.is(2)))
	            .andExpect(jsonPath("$[0].title", Matchers.is("Book Title 2")))
	            .andExpect(jsonPath("$[0].author.id", Matchers.is(2)))
	            .andExpect(jsonPath("$[0].author.name", Matchers.is("Jane Smith")))
	            .andExpect(jsonPath("$[0].author.biography", Matchers.is("An award-winning author known for her captivating storytelling.")))
	            .andExpect(jsonPath("$[0].isbn", Matchers.is("0987654321")))
	            .andExpect(jsonPath("$[0].publicationYear", Matchers.is("2023")))
	            .andExpect(jsonPath("$[0].availableForRent", Matchers.is(false)));
	}

	@Test
	@DisplayName("Should Delete Book by ID When making DELETE request to endpoint - /api/books/{id}")
	@WithMockUser(username = "test")
	void shouldDeleteBookById() throws Exception {
	    Mockito.when(bookService.deleteBook(3L)).thenReturn(true); // Assuming deletion is successful

	    mockMvc.perform(delete("/api/books/{id}", 3L))
	            .andExpect(status().isOk())
	            .andExpect(content().string("Book has been deleted successfully!"));
	}


}
