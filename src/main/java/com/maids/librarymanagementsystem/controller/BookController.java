package com.maids.librarymanagementsystem.controller;

import com.maids.librarymanagementsystem.dto.BookTO;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("books")
    public List<BookTO> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("books/{id}")
    public ResponseEntity<BookTO> getBookById(@PathVariable("id") long bookId) throws ResourceNotFoundException {
        return new ResponseEntity<BookTO>(bookService.getBookById(bookId), HttpStatus.OK);
    }

    @PostMapping("books")
    public ResponseEntity<BookTO> addBook(@RequestBody BookTO bookTO) throws BadRequestException, ConflictException {
        return new ResponseEntity<>(bookService.addBook(bookTO), HttpStatus.CREATED);
    }

    @PutMapping("books/{id}")
    public ResponseEntity<BookTO> updateBook(@PathVariable("id") long id,@RequestBody BookTO bookTO) throws ResourceNotFoundException, ConflictException {
        return new ResponseEntity<>(bookService.updateBook(id,bookTO), HttpStatus.OK);
    }

    @DeleteMapping("books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") long id) throws ConflictException, ResourceNotFoundException {
        return new ResponseEntity<String>(bookService.removeBook(id), HttpStatus.OK);
    }
}
