package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.BookTO;
import com.maids.librarymanagementsystem.dto.PatronTO;
import com.maids.librarymanagementsystem.entity.Book;
import com.maids.librarymanagementsystem.entity.Patron;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.repository.BookRepository;
import com.maids.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.maids.librarymanagementsystem.repository.PatronRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatronServiceTest {

    @InjectMocks
    private PatronServiceImpl patronService;

    @Mock
    private PatronRepository patronRepository;
    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    private Patron patron;
    private PatronTO patronTO;

    @BeforeEach
    public void setUp() {
        patron = new Patron();
        patron.setId(1L);
        patron.setName("testDATAA");
        patron.setContactInfo("000111111199");

        patronTO = new PatronTO();
        patronTO.setId(1L);
        patronTO.setName("testDATAA");
        patronTO.setContactInfo("000111111199");
    }

    @Test
    public void addPatronTest() throws ConflictException, BadRequestException {

        when(patronRepository.findByContact(patronTO.getContactInfo())).thenReturn(null);

        when(patronRepository.save(Mockito.any(Patron.class))).thenReturn(patron);

        PatronTO savedPatron = patronService.addPatron(patronTO);

        Assertions.assertThat(savedPatron.getName()).isEqualTo(patronTO.getName());
    }

    @Test
    public void getAllPatronsTest() {
        List<Patron> patrons = new ArrayList<>();
        patrons.add(patron);
        when(patronRepository.findAll()).thenReturn(patrons);

        List<PatronTO> patronTOS = patronService.getAllPatrons();

        Assertions.assertThat(patronTOS).isNotNull();
    }

    @Test
    public void getPatronByIdTest() throws ResourceNotFoundException {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        PatronTO savedPatron = patronService.getPatronById(1L);

        Assertions.assertThat(savedPatron.getName()).isEqualTo("testDATAA");
    }

    @Test
    public void updatePatronTest() throws ConflictException, ResourceNotFoundException {
        // update title
        patronTO.setName("new testDATAAAA");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(patronRepository.findByContactForExistingPatron(patronTO.getContactInfo(), 1L)).thenReturn(null);
        when(patronRepository.save(Mockito.any(Patron.class))).thenReturn(patron);

        PatronTO savedPatron = patronService.updatePatron(1L,patronTO);

        Assertions.assertThat(savedPatron.getName()).isEqualTo(patronTO.getName());
    }


    @Test
    public void deletePatronTest() throws ResourceNotFoundException, ConflictException {

        // Mock the repository behavior
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.getBorrowingRecordsCountByPatronId(1L)).thenReturn(0);
        doNothing().when(patronRepository).delete(Mockito.any(Patron.class));

        // Call the service method
        String result = patronService.removePatron(1L);

        Assertions.assertThat(result).isEqualTo("Patron Deleted Successfully!");
    }


}
