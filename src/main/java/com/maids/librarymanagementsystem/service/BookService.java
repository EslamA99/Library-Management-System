package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.BookTO;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;

import java.util.List;

public interface BookService {
    List<BookTO>getAllBooks();
    BookTO getBookById(Long id) throws ResourceNotFoundException;
    BookTO addBook(BookTO bookTO) throws BadRequestException, ConflictException;
    BookTO updateBook(Long id,BookTO bookTO) throws ResourceNotFoundException, ConflictException;
    String removeBook(Long id) throws ResourceNotFoundException, ConflictException;

}
