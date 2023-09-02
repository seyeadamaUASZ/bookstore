package com.sid.gl.repository;

import com.sid.gl.models.Book;

import java.util.List;

public interface BookCustomRespository {
    List<Book> findByTitleAndIsbnAndAuthorNameAndBookType(String title, String isbn,String authorName,String bookType);
}
