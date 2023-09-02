package com.sid.gl.repository;

import com.sid.gl.models.Book;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book,Long>, BookCustomRespository, JpaSpecificationExecutor<Book> {
    Book findByFileName(String fileName);

}
