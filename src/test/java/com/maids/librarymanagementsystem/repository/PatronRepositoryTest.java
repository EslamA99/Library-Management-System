package com.maids.librarymanagementsystem.repository;

import com.maids.librarymanagementsystem.entity.Book;
import com.maids.librarymanagementsystem.entity.Patron;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatronRepositoryTest {

    @Autowired
    private PatronRepository patronRepository;

    private Patron patron;
    @BeforeEach
    public void setUp() {
        patron = new Patron();
        patron.setName("testDATAAAA");
        patron.setContactInfo("000111111199");
    }

    @Test
    public void savePatronTest() {
        Patron savedPatron=patronRepository.save(patron);
        Assertions.assertThat(savedPatron).isNotNull();
        Assertions.assertThat(savedPatron.getId()).isGreaterThan(0);
    }

    @Test
    public void getPatronTest() {
        Patron savedPatron=patronRepository.save(patron);
        Assertions.assertThat(savedPatron.getId()).isGreaterThan(0);

        Patron foundPatron = patronRepository.findById(savedPatron.getId()).orElse(null);
        Assertions.assertThat(foundPatron).isNotNull();
        Assertions.assertThat(foundPatron.getId()).isGreaterThan(0);
        Assertions.assertThat(foundPatron.getId()).isEqualTo(savedPatron.getId());
    }

    @Test
    public void getListOfPatronsTest() {
        patronRepository.save(patron);

        List<Patron> patrons = patronRepository.findAll();
        Assertions.assertThat(patrons).isNotNull();
        Assertions.assertThat(patrons.size()).isGreaterThan(0);
    }

    @Test
    public void updatePatronTest() {

        Patron savedPatron=patronRepository.save(patron);

        Patron returnedPatron = patronRepository.findById(savedPatron.getId()).orElse(null);
        Assertions.assertThat(returnedPatron).isNotNull();
        Assertions.assertThat(returnedPatron.getId()).isGreaterThan(0);

        returnedPatron.setName("testDATAAAAA");
        Patron updatedPatron = patronRepository.save(returnedPatron);
        Assertions.assertThat(updatedPatron).isNotNull();
        Assertions.assertThat(updatedPatron.getId()).isGreaterThan(0);
        Assertions.assertThat(updatedPatron.getName()).isEqualTo("testDATAAAAA");
    }

    @Test
    public void deletePatronTest() {
        Patron savedPatron=patronRepository.save(patron);

        Patron returnedPatron = patronRepository.findById(savedPatron.getId()).orElse(null);
        Assertions.assertThat(returnedPatron).isNotNull();
        Assertions.assertThat(returnedPatron.getId()).isGreaterThan(0);

        patronRepository.delete(returnedPatron);
        Optional<Patron> optionalPatron = patronRepository.findById(returnedPatron.getId());
        Assertions.assertThat(optionalPatron).isEmpty();
    }
}