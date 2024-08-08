package com.maids.librarymanagementsystem.repository;

import com.maids.librarymanagementsystem.entity.Book;
import com.maids.librarymanagementsystem.entity.BorrowingRecord;
import com.maids.librarymanagementsystem.entity.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BorrowingRecordRepositoryTest {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setTitle("testBook");
        book.setAuthor("testBook");
        book.setIsbn("111111111111111");
        book.setPublicationYear(2020);
        bookRepository.save(book);

        patron = new Patron();
        patron.setName("testPatron");
        patron.setContactInfo("000111111199");
        patronRepository.save(patron);

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(new Date());
        borrowingRecordRepository.save(borrowingRecord);
    }

    @Test
    public void findByBookIdAndPatronIdTest() {
        BorrowingRecord foundRecord = borrowingRecordRepository.findByBookIdAndPatronId(book.getId(), patron.getId());
        assertThat(foundRecord).isNotNull();
        assertThat(foundRecord.getBook().getTitle()).isEqualTo("testBook");
        assertThat(foundRecord.getPatron().getName()).isEqualTo("testPatron");
    }

    @Test
    public void saveBorrowingRecordTest() {
        BorrowingRecord newRecord = new BorrowingRecord();
        newRecord.setBook(book);
        newRecord.setPatron(patron);
        newRecord.setBorrowingDate(new Date());
        BorrowingRecord savedRecord = borrowingRecordRepository.save(newRecord);
        assertThat(savedRecord).isNotNull();
        assertThat(savedRecord.getId()).isNotNull();
    }
}