package com.maids.librarymanagementsystem.controller;

import com.maids.librarymanagementsystem.dto.BorrowingRecordTO;
import com.maids.librarymanagementsystem.service.BorrowingRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowingRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    private BorrowingRecordTO borrowingRecordTO;

    @BeforeEach
    public void setUp() {
        borrowingRecordTO = new BorrowingRecordTO();
        borrowingRecordTO.setId(1L);
        borrowingRecordTO.setBookId(1L);
        borrowingRecordTO.setPatronId(1L);
        borrowingRecordTO.setBorrowingDate(new java.util.Date());
    }

    @Test
    public void addBook_BorrowSuccess() throws Exception {
        when(borrowingRecordService.borrowBook(1L, 1L)).thenReturn(borrowingRecordTO);

        mockMvc.perform(post("/api/borrow/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.patronId").value(1L));
    }

    @Test
    public void updateBook_ReturnSuccess() throws Exception {
        borrowingRecordTO.setReturnDate(new java.util.Date());
        when(borrowingRecordService.returnBook(1L, 1L)).thenReturn(borrowingRecordTO);

        mockMvc.perform(put("/api/return/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.patronId").value(1L))
                .andExpect(jsonPath("$.returnDate").isNotEmpty());
    }
}