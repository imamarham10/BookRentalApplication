package com.olik.bookrentalapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olik.bookrentalapp.models.Book;
import com.olik.bookrentalapp.models.Rental;
import com.olik.bookrentalapp.repository.BookRepository;
import com.olik.bookrentalapp.repository.RentalRepository;

@Service
public class RentalService {
	@Autowired
	private RentalRepository rentalRepository;
	@Autowired
	private BookRepository bookRepository;

	public List<Rental> getAllRentals(){
		return rentalRepository.findAll();
	}
	public Rental getRentalById(Long id) {
		Optional<Rental> rentalOptional = rentalRepository.findById(id);
		return rentalOptional.orElse(null);
	}
	public List<Rental>getRentalByRenterName(String renterName){
		return rentalRepository.getRentalsByRenterName(renterName);
	}
	public Rental addRental(Rental rental, Long book_id) {
		Optional<Book> bookOptional = bookRepository.findById(book_id);
		if(bookOptional.isPresent()) {
			Book book = bookOptional.get();
			if(isBookAvailableFoRent(book)) {
				book.setAvailableForRent(false);
				Rental saveRental = new Rental(rental.getRenterName(),rental.getRentalDate(),rental.getReturnDate(),book);
				
				return rentalRepository.save(saveRental);
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
	private boolean isBookAvailableFoRent(Book book) {
		// TODO Auto-generated method stub
		return book.isAvailableForRent() || allRentalsReturned(book);
	}
	private boolean allRentalsReturned(Book book) {
		for(Rental rental: book.getRentals()) {
			if(rental.getReturnDate()==null) {
				return false;
			}
		}
		return true;
	}
	public Rental updateRental(Long id, Rental rental) {
		Optional<Rental> existingRentalOptional = rentalRepository.findById(id);
        if (existingRentalOptional.isPresent()) {
            Rental existingRental = existingRentalOptional.get();
            
            if (rental.getReturnDate().isBefore(existingRental.getRentalDate())) {
                throw new IllegalArgumentException("Return date cannot be earlier than rental date");
            }
            existingRental.getBook().setAvailableForRent(true);
            existingRental.setReturnDate(rental.getReturnDate());
            return rentalRepository.save(existingRental);
        } else {
            return null;
        }
	}
	public boolean deleteRental(Long id) {
		if(rentalRepository.existsById(id)) {
			rentalRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}
	
	public List<Rental> getOverdueRentals(){
		List<Rental> overdueRentals = new ArrayList<>();
        List<Rental> allRentals = rentalRepository.findAll(); // Retrieve all rentals

        for (Rental rental : allRentals) {
            LocalDate rentalDate = rental.getRentalDate();
            LocalDate currentDate = LocalDate.now();
            long daysSinceRental = java.time.temporal.ChronoUnit.DAYS.between(rentalDate, currentDate);

            // Check if rental is overdue based on rental date and current date
            if (daysSinceRental > 14) {
                overdueRentals.add(rental);
                continue; // Skip the rest of the checks if rental is already overdue
            }

            LocalDate returnDate = rental.getReturnDate();
            if (returnDate != null) {
                // Check if rental is overdue based on rental date and return date
                long daysSinceReturn = java.time.temporal.ChronoUnit.DAYS.between(rentalDate, returnDate);
                if (daysSinceReturn > 14) {
                    overdueRentals.add(rental);
                }
            }
        }
        return overdueRentals;
	}
}
