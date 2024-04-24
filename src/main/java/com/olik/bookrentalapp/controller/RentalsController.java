package com.olik.bookrentalapp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olik.bookrentalapp.models.Rental;
import com.olik.bookrentalapp.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalsController {
	@Autowired
	private RentalService rentalService;

	@PostMapping
	public ResponseEntity<?> addRental(@RequestBody Rental rental, @RequestParam Long book_id){
		if(rental == null || book_id == null) {
			return ResponseEntity.badRequest().body("Rental details and book id are required!");
		}
		Rental savedRental = rentalService.addRental(rental, book_id);
		if(savedRental != null) {
			return ResponseEntity.created(URI.create("/api/rentals/" + savedRental.getId())).body(savedRental);
		}else {
			return ResponseEntity.status(500).body("Failed to add rental with this book id: " + book_id
					+ "( Possible reason: Book might not be available for rent)");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllRentals(){
		try {
			List<Rental> rentals = rentalService.getAllRentals();
			return ResponseEntity.ok(rentals);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve rentals: " + e.getMessage());
		}
	}

	@GetMapping("/by-renterName")
	public ResponseEntity<?> getRentalsByRenterName(@RequestParam String name){
		try {
			List<Rental> rentals = rentalService.getRentalByRenterName(name);
			return ResponseEntity.ok(rentals);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve rentals by renter name: " + e.getMessage());
		}
	}

	@GetMapping("/overdue-rentals")
	public ResponseEntity<?> getOverdueRentals(){
		try {
			List<Rental> overdueRentals = rentalService.getOverdueRentals();
			return ResponseEntity.ok(overdueRentals);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve overdue rentals: " + e.getMessage());
		}
	}
	
	@PutMapping
	public ResponseEntity<?> updateRental(@RequestParam Long rental_id, @RequestBody Rental rental){
		try {
	        Rental updatedRental = rentalService.updateRental(rental_id, rental);
	        if (updatedRental != null) {
	            return ResponseEntity.ok(updatedRental);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the rental.");
	    }
	}
	@DeleteMapping
	public ResponseEntity<String> deleteRental(@RequestParam Long rental_id){
		boolean rentalDeleted = rentalService.deleteRental(rental_id);
		if(rentalDeleted) {
			return ResponseEntity.ok("Rental record with id " + rental_id + " has been deleted!");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Rental record with id " + rental_id + " not found.");
		}
	}
}
