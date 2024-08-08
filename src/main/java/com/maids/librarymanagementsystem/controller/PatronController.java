package com.maids.librarymanagementsystem.controller;

import com.maids.librarymanagementsystem.dto.PatronTO;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class PatronController {
    @Autowired
    PatronService patronService;

    @GetMapping("patrons")
    public List<PatronTO> getAllPatrons(){
        return patronService.getAllPatrons();
    }

    @GetMapping("patrons/{id}")
    public ResponseEntity<PatronTO> getPatronById(@PathVariable("id") long patronId) throws ResourceNotFoundException {
        return new ResponseEntity<PatronTO>(patronService.getPatronById(patronId), HttpStatus.OK);
    }

    @PostMapping("patrons")
    public ResponseEntity<PatronTO> addPatron(@RequestBody PatronTO patronTO) throws BadRequestException, ConflictException {
        return new ResponseEntity<>(patronService.addPatron(patronTO), HttpStatus.CREATED);
    }

    @PutMapping("patrons/{id}")
    public ResponseEntity<PatronTO> updatePatron(@PathVariable("id") long id,@RequestBody PatronTO patronTO) throws ResourceNotFoundException, ConflictException {
        return new ResponseEntity<>(patronService.updatePatron(id,patronTO), HttpStatus.OK);
    }

    @DeleteMapping("patrons/{id}")
    public ResponseEntity<String> deletePatron(@PathVariable("id") long id) throws ConflictException, ResourceNotFoundException {
        return new ResponseEntity<String>(patronService.removePatron(id), HttpStatus.OK);
    }
}
