package com.maids.librarymanagementsystem.controller;

import com.maids.librarymanagementsystem.dto.BookTO;
import com.maids.librarymanagementsystem.dto.BorrowingRecordTO;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class BorrowingRecordController {
    @Autowired
    BorrowingRecordService borrowingRecordService;

    @PostMapping("borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordTO> addBook(@PathVariable("bookId") long bookId,
                                          @PathVariable("patronId") long patronId) throws ConflictException, ResourceNotFoundException {
        return new ResponseEntity<>(borrowingRecordService.borrowBook(bookId,patronId), HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordTO> updateBook(@PathVariable("bookId") long bookId,
                                             @PathVariable("patronId") long patronId) throws ResourceNotFoundException {
        return new ResponseEntity<>(borrowingRecordService.returnBook(bookId,patronId), HttpStatus.OK);
    }
}
