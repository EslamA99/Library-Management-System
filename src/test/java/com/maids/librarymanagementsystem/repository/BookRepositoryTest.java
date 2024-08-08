package com.maids.librarymanagementsystem.repository;

import com.maids.librarymanagementsystem.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setTitle("testDATAA");
        book.setAuthor("testDATAA");
        book.setIsbn("111111111111111");
        book.setPublicationYear(2020);
    }

    @Test
    public void saveBookTest() {
        Book savedBook=bookRepository.save(book);
        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getId()).isGreaterThan(0);
    }

    @Test
    public void getBookTest() {
        Book savedBook=bookRepository.save(book);
        Assertions.assertThat(savedBook.getId()).isGreaterThan(0);

        Book foundBook = bookRepository.findById(savedBook.getId()).orElse(null);
        Assertions.assertThat(foundBook).isNotNull();
        Assertions.assertThat(foundBook.getId()).isGreaterThan(0);
        Assertions.assertThat(foundBook.getId()).isEqualTo(savedBook.getId());
    }

    @Test
    public void getListOfBooksTest() {
        bookRepository.save(book);

        List<Book> books = bookRepository.findAll();
        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isGreaterThan(0);
    }

    @Test
    public void updateBookTest() {

        Book savedBook=bookRepository.save(book);

        Book returnedBook = bookRepository.findById(savedBook.getId()).orElse(null);
        Assertions.assertThat(returnedBook).isNotNull();
        Assertions.assertThat(returnedBook.getId()).isGreaterThan(0);

        returnedBook.setIsbn("4567");
        Book updatedBook = bookRepository.save(returnedBook);
        Assertions.assertThat(updatedBook).isNotNull();
        Assertions.assertThat(updatedBook.getId()).isGreaterThan(0);
        Assertions.assertThat(updatedBook.getIsbn()).isEqualTo("4567");
    }

    @Test
    public void deleteBookTest() {
        Book savedBook=bookRepository.save(book);

        Book returnedBook = bookRepository.findById(savedBook.getId()).orElse(null);
        Assertions.assertThat(returnedBook).isNotNull();
        Assertions.assertThat(returnedBook.getId()).isGreaterThan(0);

        bookRepository.delete(returnedBook);
        Optional<Book> optionalBook = bookRepository.findById(returnedBook.getId());
        Assertions.assertThat(optionalBook).isEmpty();
    }
}