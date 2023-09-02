package com.sid.gl.specifications;

import com.sid.gl.models.Book;
import com.sid.gl.models.Book_;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> hasTitle(String title){
        return ((root,criteriaQuery,criteriaBuilder)-> criteriaBuilder.equal(root.get(Book_.TITLE),title));
    }

    public static Specification<Book> hasISBN(String isbn){
        return ((root,criteriaQuery,criteriaBuilder)->criteriaBuilder.equal(root.get(Book_.ISBN),isbn));
    }

    public static Specification<Book> hasBookType(String bookType){
        return ((root,criteriaQuery,criteriaBuilder)->criteriaBuilder.equal(root.get(Book_.BOOK_TYPE),bookType));
    }

    public static Specification<Book> containsBookType(String bookType){
        return ((root,criteriaQuery,criteriaBuilder)->criteriaBuilder.like(root.get(Book_.BOOK_TYPE),"%"+bookType+"%"));
    }


}
