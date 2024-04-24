package com.olik.bookrentalapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olik.bookrentalapp.models.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>{
	@Query(value = "select r from Rental r where r.renterName=?1")
	List<Rental> getRentalsByRenterName(String renterName);
}
