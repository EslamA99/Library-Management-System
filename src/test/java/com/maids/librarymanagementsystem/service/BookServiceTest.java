package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.BookTO;
import com.maids.librarymanagementsystem.entity.Book;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.repository.BookRepository;
import com.maids.librarymanagementsystem.repository.BorrowingRecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    private Book book;
    private BookTO bookTO;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("testDATAA");
        book.setAuthor("testDATAA");
        book.setIsbn("111111111111111");
        book.setPublicationYear(2020);

        bookTO = new BookTO();
        bookTO.setId(1L);
        bookTO.setTitle("testDATAA");
        bookTO.setAuthor("testDATAA");
        bookTO.setIsbn("111111111111111");
        bookTO.setPublicationYear(2020);
    }

    @Test
    public void addBookTest() throws ConflictException, BadRequestException {

        when(bookRepository.findByIsbn(bookTO.getIsbn())).thenReturn(null);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookTO savedBook = bookService.addBook(bookTO);

        Assertions.assertThat(savedBook.getTitle()).isEqualTo(bookTO.getTitle());
    }

    @Test
    public void getAllBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(book);

        when(bookRepository.findAll()).thenReturn(books);

        List<BookTO> bookTOS = bookService.getAllBooks();

        Assertions.assertThat(bookTOS).isNotNull();
    }

    @Test
    public void getBookByIdTest() throws ResourceNotFoundException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookTO savedBook = bookService.getBookById(1L);

        Assertions.assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void updateBookTest() throws ConflictException, ResourceNotFoundException {
        // update title
        bookTO.setTitle("testDATAAAA");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbnForExistingBook(bookTO.getIsbn(), 1L)).thenReturn(null);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookTO savedBook = bookService.updateBook(1L,bookTO);

        Assertions.assertThat(savedBook.getTitle()).isEqualTo("testDATAAAA");
    }


    @Test
    public void deleteBookTest() throws ResourceNotFoundException, ConflictException {
        book.setId(1L);

        // Mock the repository behavior
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.getBorrowingRecordsCountByBookId(1L)).thenReturn(0);
        doNothing().when(bookRepository).delete(Mockito.any(Book.class));

        // Call the service method
        String result = bookService.removeBook(1L);

        Assertions.assertThat(result).isEqualTo("Book Deleted Successfully!");
    }


}
