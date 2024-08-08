package com.maids.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.librarymanagementsystem.dto.PatronTO;
import com.maids.librarymanagementsystem.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatronController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    private PatronTO patronTO;

    @BeforeEach
    public void setUp() {
        patronTO = new PatronTO();
        patronTO.setId(1L);
        patronTO.setName("Test Patron");
        patronTO.setContactInfo("000111111199");
    }

    @Test
    public void getAllPatronsTest() throws Exception {
        List<PatronTO> patronList = Arrays.asList(patronTO);
        when(patronService.getAllPatrons()).thenReturn(patronList);

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test Patron"));
    }

    @Test
    public void getPatronByIdTest() throws Exception {
        when(patronService.getPatronById(1L)).thenReturn(patronTO);

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Patron"));
    }

    @Test
    public void addPatronTest() throws Exception {
        when(patronService.addPatron(Mockito.any(PatronTO.class))).thenReturn(patronTO);

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patronTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Patron"));
    }

    @Test
    public void updatePatronTest() throws Exception {
        when(patronService.updatePatron(Mockito.anyLong(), Mockito.any(PatronTO.class))).thenReturn(patronTO);

        mockMvc.perform(put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patronTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Patron"));
    }

    @Test
    public void deletePatronTest() throws Exception {
        when(patronService.removePatron(1L)).thenReturn("Patron Deleted Successfully!");

        mockMvc.perform(delete("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patron Deleted Successfully!"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}