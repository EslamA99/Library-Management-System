package com.maids.librarymanagementsystem.repository;

import com.maids.librarymanagementsystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("select book from Book book where book.isbn=:isbn ")
    Book findByIsbn(@Param("isbn")String isbn);

    @Query("select book from Book book where book.isbn=:isbn and book.id<>:id ")
    Book findByIsbnForExistingBook(@Param("isbn")String isbn,@Param("id") Long id);


//    @Query("select book from Book book where book.id=:id ")
//    Book getById(@Param("id") Long id);

}
