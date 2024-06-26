package com.olik.bookrentalapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.olik.bookrentalapp.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{

}
