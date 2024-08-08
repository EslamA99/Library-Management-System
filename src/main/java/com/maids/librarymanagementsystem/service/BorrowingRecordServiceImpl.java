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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {
    private PatronRepository patronRepository;
    private BookRepository bookRepository;
    private BorrowingRecordRepository borrowingRecordRepository;

    public BorrowingRecordServiceImpl(PatronRepository patronRepository, BookRepository bookRepository, BorrowingRecordRepository borrowingRecordRepository) {
        this.patronRepository = patronRepository;
        this.bookRepository = bookRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    @Transactional
    @Override
    public BorrowingRecordTO borrowBook(Long bookId, Long patronId) throws ResourceNotFoundException, ConflictException {
        try {
            BorrowingRecord checkSavedRecord=borrowingRecordRepository.findByBookIdAndPatronId(bookId,patronId);
            if(checkSavedRecord!=null){
                throw new ConflictException("Borrowing Record already found for same book and patron.");
            }
            Book book=bookRepository.findById(bookId).
                    orElseThrow(()->new ResourceNotFoundException("no Book found with id = "+bookId));

            Patron patron=patronRepository.findById(patronId).
                    orElseThrow(()->new ResourceNotFoundException("no Patron found with id = "+patronId));
            BorrowingRecord borrowingRecord=new BorrowingRecord();
            borrowingRecord.setBorrowingDate(new Date());
            borrowingRecord.setBook(book);
            borrowingRecord.setPatron(patron);
            borrowingRecord.setReturnDate(null);
            BorrowingRecord savedRecord=borrowingRecordRepository.save(borrowingRecord);
            return convertToTO(savedRecord);
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (ConflictException conflictException){
            throw conflictException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }

    }

    @Transactional
    @Override
    public BorrowingRecordTO returnBook(Long bookId, Long patronId) throws ResourceNotFoundException {
        try {
            BorrowingRecord savedRecord=borrowingRecordRepository.findByBookIdAndPatronId(bookId,patronId);
            if(savedRecord==null){
                throw new ResourceNotFoundException("No Borrowing Record found for this book and patron.");
            }
            savedRecord.setReturnDate(new Date());
            return convertToTO(borrowingRecordRepository.save(savedRecord));
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    private BorrowingRecordTO convertToTO(BorrowingRecord borrowingRecord) {
        BorrowingRecordTO borrowingRecordTO = new BorrowingRecordTO();
        borrowingRecordTO.setId(borrowingRecord.getId());
        borrowingRecordTO.setBookId(borrowingRecord.getBook().getId());
        borrowingRecordTO.setPatronId(borrowingRecord.getPatron().getId());
        borrowingRecordTO.setBorrowingDate(borrowingRecord.getBorrowingDate());
        borrowingRecordTO.setReturnDate(borrowingRecord.getReturnDate());
        return borrowingRecordTO;
    }

    private BorrowingRecord convertToEntity(BorrowingRecordTO borrowingRecordTO) throws ResourceNotFoundException {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(borrowingRecordTO.getId());
        borrowingRecord.setBorrowingDate(borrowingRecordTO.getBorrowingDate());
        borrowingRecord.setReturnDate(borrowingRecordTO.getReturnDate());

        Book book = bookRepository.findById(borrowingRecordTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + borrowingRecordTO.getBookId()));
        Patron patron = patronRepository.findById(borrowingRecordTO.getPatronId())
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found with id: " + borrowingRecordTO.getPatronId()));

        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);

        return borrowingRecord;
    }
}
