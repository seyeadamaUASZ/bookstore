package com.sid.gl.repository;

import com.sid.gl.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
    Book findByFileName(String fileName);

}
