package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.BorrowingRecordTO;
import com.maids.librarymanagementsystem.entity.Book;
import com.maids.librarymanagementsystem.entity.BorrowingRecord;
import com.maids.librarymanagementsystem.entity.Patron;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.repository.BookRepository;
import com.maids.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.maids.librarymanagementsystem.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BorrowingRecordServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingRecordServiceImpl borrowingRecordService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("testDATAA");
        book.setAuthor("testDATAA");
        book.setIsbn("111111111111111");
        book.setPublicationYear(2020);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("testDATAA");
        patron.setContactInfo("000111111199");

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(new Date());
    }

    @Test
    public void borrowBookTest_Success() throws ResourceNotFoundException, ConflictException {
        when(borrowingRecordRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(null);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecordTO result = borrowingRecordService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals(1L, result.getPatronId());
        assertNotNull(result.getBorrowingDate());
        assertNull(result.getReturnDate());
    }

    @Test
    public void returnBookTest_Success() throws ResourceNotFoundException {
        when(borrowingRecordRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(borrowingRecord);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecordTO result = borrowingRecordService.returnBook(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals(1L, result.getPatronId());
        assertNotNull(result.getReturnDate());
    }
}