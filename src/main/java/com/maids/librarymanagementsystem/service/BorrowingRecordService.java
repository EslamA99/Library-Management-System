package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.BorrowingRecordTO;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;

public interface BorrowingRecordService {
    BorrowingRecordTO borrowBook(Long bookId,Long patronId) throws ResourceNotFoundException, ConflictException;
    BorrowingRecordTO returnBook(Long bookId,Long patronId) throws ResourceNotFoundException;
}
