package com.olik.bookrentalapp.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olik.bookrentalapp.models.Author;
import com.olik.bookrentalapp.service.AuthorService;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = AuthorController.class)
public class AuthorControllerTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Should Add Author When making POST request to endpoint - /api/author")
    void shouldAddAuthor() throws Exception {
        Author authorRequest = new Author();
        authorRequest.setName("John Doe");
        authorRequest.setBiography("Test Author");

        Author savedAuthorResponse = new Author();
        savedAuthorResponse.setId(1L);
        savedAuthorResponse.setName("John Doe");
        savedAuthorResponse.setBiography("Test Author");

        Mockito.when(authorService.addAuthor(any(Author.class))).thenReturn(savedAuthorResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("John Doe")))
                .andExpect(jsonPath("$.biography", Matchers.is("Test Author")));
    }

    @Test
    @DisplayName("Should Update Author When making PUT request to endpoint - /api/author")
    void shouldUpdateAuthor() throws Exception {
        // Given
        Author updatedAuthorRequest = new Author();
        updatedAuthorRequest.setName("Test Author");
        updatedAuthorRequest.setBiography("Test data");

        Author updatedAuthorResponse = new Author();
        updatedAuthorResponse.setId(3L);
        updatedAuthorResponse.setName("Test Author");
        updatedAuthorResponse.setBiography("Test data");

        Mockito.when(authorService.updateAuthor(eq(3L), any(Author.class))).thenReturn(updatedAuthorResponse);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/author?author_id=3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAuthorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.name", Matchers.is("Test Author")))
                .andExpect(jsonPath("$.biography", Matchers.is("Test data")));
    }

    
    @Test
    @DisplayName("Should Retrieve all Author making GET request to endpoint - /api/authors")
    public void getAllAuthors() throws Exception {
        // Mocked list of authors
        List<Author> authors = Arrays.asList(
            new Author(1L, "John Doe", "An acclaimed author with several best-selling novels."),
            new Author(2L, "Jane Smith", "An award-winning author known for her captivating storytelling.")
        );

        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/api/author")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].biography").value("An acclaimed author with several best-selling novels."))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].biography").value("An award-winning author known for her captivating storytelling."));
    }
    
    @Test
    @DisplayName("Should Retrieve Author by ID When making GET request to endpoint - /api/authors/{id}")
    @WithMockUser(username = "test")
    void shouldRetrieveAuthorById() throws Exception {
        Author authorResponse = new Author(1L, "John Doe", "An acclaimed author with several best-selling novels.");

        Mockito.when(authorService.getAuthorById(1L)).thenReturn(authorResponse);

        mockMvc.perform(get("/api/author/by-id?author_id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("John Doe")))
                .andExpect(jsonPath("$.biography", Matchers.is("An acclaimed author with several best-selling novels.")));
    }
    
    @Test
    @DisplayName("Should Delete Author by ID When making DELETE request to endpoint - /api/author/by-id")
    @WithMockUser(username = "test")
    void shouldDeleteAuthorById() throws Exception {
        Long authorId = 2L;

        Mockito.when(authorService.deleteAuthor(authorId)).thenReturn(true);

        mockMvc.perform(delete("/api/author?author_id=" + authorId))
                .andExpect(status().isOk())
                .andExpect(content().string("Author deleted with id: " + authorId));
    }
}
