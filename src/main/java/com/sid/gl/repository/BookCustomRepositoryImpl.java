package com.sid.gl.repository;

import com.sid.gl.models.Book;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class BookCustomRepositoryImpl implements BookCustomRespository{
   @Autowired
    private EntityManager entityManager;
    @Override
    public List<Book> findByTitleAndIsbnAndAuthorNameAndBookType(String title, String isbn, String authorName, String bookType) {
        return null;
    }
}
