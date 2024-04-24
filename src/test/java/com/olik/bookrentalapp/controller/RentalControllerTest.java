package com.olik.bookrentalapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.olik.bookrentalapp.models.Rental;
import com.olik.bookrentalapp.service.RentalService;

@WebMvcTest(controllers = RentalsController.class)
public class RentalControllerTest {
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new Jdk8Module());
    }

    @MockBean
    private RentalService rentalService;
    
    @Test
    @DisplayName("Should Add Rental When making POST request to endpoint - /api/rentals/")
    void shouldAddRental() throws Exception {
        // Given
        Rental rentalRequest = new Rental();
        rentalRequest.setRenterName("John");
        rentalRequest.setRentalDate(LocalDate.of(2024, 3, 30));
        rentalRequest.setReturnDate(null);

        Rental savedRentalResponse = new Rental();
        savedRentalResponse.setId(1L);
        savedRentalResponse.setRenterName("John");
        savedRentalResponse.setRentalDate(LocalDate.of(2024, 3, 30));
        savedRentalResponse.setReturnDate(null);

        Mockito.when(rentalService.addRental(any(Rental.class), eq(4L))).thenReturn(savedRentalResponse);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rentals?book_id=4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentalRequest)))
                .andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("Should Update Rental When making PUT request to endpoint - /api/rentals/")
    void shouldUpdateRental() throws Exception {
        // Given
        Rental updatedRentalRequest = new Rental();
        updatedRentalRequest.setReturnDate(LocalDate.of(2024, 4, 23));

        Rental updatedRentalResponse = new Rental();
        updatedRentalResponse.setId(8L);
        updatedRentalResponse.setReturnDate(LocalDate.of(2024, 4, 23));

        Mockito.when(rentalService.updateRental(eq(8L), any(Rental.class))).thenReturn(updatedRentalResponse);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/rentals?rental_id=8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedRentalRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(8)))
                .andExpect(jsonPath("$.returnDate", Matchers.is("2024-04-23")));
    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    @DisplayName("Should List All Rentals When making GET request to endpoint - /api/rentals/")
    @WithMockUser(username = "test")
    void shouldListRentals() throws Exception {
    	 Rental rental1 = new Rental("John", LocalDate.of(2024, 3, 30), null, null);
         Rental rental2 = new Rental("Jane", LocalDate.of(2024, 4, 1), null, null);

        Mockito.when(rentalService.getAllRentals()).thenReturn(asList(rental1, rental2));

        mockMvc.perform(get("/api/rentals"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].renterName", Matchers.is("John")))
                .andExpect(jsonPath("$[0].rentalDate", Matchers.is("2024-03-30")))
                .andExpect(jsonPath("$[0].returnDate").doesNotExist()) // Assuming returnDate is not set
                .andExpect(jsonPath("$[1].renterName", Matchers.is("Jane")))
                .andExpect(jsonPath("$[1].rentalDate", Matchers.is("2024-04-01")))
                .andExpect(jsonPath("$[1].returnDate").doesNotExist()); // Assuming returnDate is not set
    }
    
    
    @Test
    @DisplayName("Should List All Rentals by Renter Name When making GET request to endpoint - /api/rentals/by-renterName")
    void shouldListRentalsByRenterName() throws Exception {
         Rental rental1 = new Rental("Arham Imam", LocalDate.of(2024, 3, 30), null, null);
        Rental rental2 = new Rental("Arham Imam", LocalDate.of(2024, 4, 1), null, null);
        List<Rental> rentals = Arrays.asList(rental1, rental2);

        // Mock rentalService behavior
        when(rentalService.getRentalByRenterName("Arham Imam")).thenReturn(rentals);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/rentals/by-renterName?name=Arham Imam"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].renterName", Matchers.is("Arham Imam")))
                .andExpect(jsonPath("$[0].rentalDate", Matchers.is("2024-03-30")))
                .andExpect(jsonPath("$[0].returnDate").doesNotExist()) // Assuming returnDate is not set
                .andExpect(jsonPath("$[1].renterName", Matchers.is("Arham Imam")))
                .andExpect(jsonPath("$[1].rentalDate", Matchers.is("2024-04-01")))
                .andExpect(jsonPath("$[1].returnDate").doesNotExist()); // Assuming returnDate is not set
    }
    
    @Test
    @DisplayName("Should List All Overdue Rentals When making GET request to endpoint - /api/rentals/overdue-rentals")
    void shouldListOverdueRentals() throws Exception {
        Rental overdueRental1 = new Rental("John Doe", LocalDate.now().minusDays(10), null, null);
        Rental overdueRental2 = new Rental("Jane Smith", LocalDate.now().minusDays(5), null, null);
        List<Rental> overdueRentals = Arrays.asList(overdueRental1, overdueRental2);

        // Mock rentalService behavior
        when(rentalService.getOverdueRentals()).thenReturn(overdueRentals);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/rentals/overdue-rentals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].renterName", Matchers.is("John Doe")))
                .andExpect(jsonPath("$[0].rentalDate", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].returnDate").doesNotExist()) // Assuming returnDate is not set
                .andExpect(jsonPath("$[1].renterName", Matchers.is("Jane Smith")))
                .andExpect(jsonPath("$[1].rentalDate", Matchers.notNullValue()))
                .andExpect(jsonPath("$[1].returnDate").doesNotExist()); // Assuming returnDate is not set
    }
    
    @Test
    @DisplayName("Should Delete Rental When making DELETE request to endpoint - /api/rentals")
    void shouldDeleteRental() throws Exception {
        Long rentalId = 6L;

        doReturn(true).when(rentalService).deleteRental(rentalId);

        mockMvc.perform(delete("/api/rentals")
                    .param("rental_id", rentalId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Rental record with id " + rentalId + " has been deleted!"));
    }
    
}
