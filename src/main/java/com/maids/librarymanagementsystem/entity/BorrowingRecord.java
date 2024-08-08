package com.maids.librarymanagementsystem.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BORROWING_RECORD")
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "BORROWING_DATE",nullable = false)
    private Date borrowingDate;

    @Column(name = "RETURN_DATE")
    private Date returnDate;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID",nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "PATRON_ID",nullable = false)
    private Patron patron;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Date borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }
}
