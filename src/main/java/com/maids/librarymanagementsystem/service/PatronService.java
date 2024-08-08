package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.PatronTO;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;

import java.util.List;

public interface PatronService {
    List<PatronTO> getAllPatrons();
    PatronTO getPatronById(Long id) throws ResourceNotFoundException;
    PatronTO addPatron(PatronTO PatronTO) throws BadRequestException, ConflictException;
    PatronTO updatePatron(Long id,PatronTO PatronTO) throws ResourceNotFoundException, ConflictException;
    String removePatron(Long id) throws ResourceNotFoundException, ConflictException;
}
