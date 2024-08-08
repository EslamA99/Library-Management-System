package com.maids.librarymanagementsystem.controller;

import com.maids.librarymanagementsystem.dto.BookTO;
import com.maids.librarymanagementsystem.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private BookTO bookTO;

    @BeforeEach
    public void setUp() {
        bookTO = new BookTO();
        bookTO.setId(1L);
        bookTO.setTitle("Test Book");
        bookTO.setAuthor("Test Author");
        bookTO.setIsbn("1234567890");
        bookTO.setPublicationYear(2020);
    }

    @Test
    public void getAllBooksTest() throws Exception {
        List<BookTO> bookList = Arrays.asList(bookTO);
        when(bookService.getAllBooks()).thenReturn(bookList);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    public void getBookByIdTest() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(bookTO);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    public void addBookTest() throws Exception {
        when(bookService.addBook(Mockito.any(BookTO.class))).thenReturn(bookTO);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(bookTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    public void updateBookTest() throws Exception {
        when(bookService.updateBook(Mockito.anyLong(), Mockito.any(BookTO.class))).thenReturn(bookTO);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(bookTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    public void deleteBookTest() throws Exception {
        when(bookService.removeBook(1L)).thenReturn("Book Deleted Successfully!");

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Deleted Successfully!"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}