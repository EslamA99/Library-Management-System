package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.BookTO;
import com.maids.librarymanagementsystem.entity.Book;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.repository.BookRepository;
import com.maids.librarymanagementsystem.repository.BorrowingRecordRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    private BookRepository bookRepository;
    private BorrowingRecordRepository borrowingRecordRepository;

    public BookServiceImpl(BookRepository bookRepository, BorrowingRecordRepository borrowingRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    @Cacheable("getAllBooks")
    @Override
    public List<BookTO> getAllBooks() {
        List<Book>bookList=bookRepository.findAll();
        List<BookTO>bookTOList=new ArrayList<>();
        if(bookList.size()==0)
            return bookTOList;

        for (Book book : bookList) {
            bookTOList.add(convertToTO(book));
        }
        return bookTOList;
    }

    @Cacheable("getBookById")
    @Override
    public BookTO getBookById(Long id) throws ResourceNotFoundException {
        try {
            Book book=bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("no Book found with id = "+id));
            return convertToTO(book);
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    @Override
    public BookTO addBook(BookTO bookTO) throws ConflictException, BadRequestException {
        try {
            if(bookTO.getTitle()==null || bookTO.getTitle().trim().isEmpty() ||
                    bookTO.getAuthor()==null || bookTO.getAuthor().trim().isEmpty() ||
                    bookTO.getIsbn()==null || bookTO.getIsbn().trim().isEmpty() ||
                    bookTO.getPublicationYear()<=0){
                throw new BadRequestException("Invalid Book Data");
            }
            if(bookRepository.findByIsbn(bookTO.getIsbn())!=null){
                throw new ConflictException("Isbn already added before");
            }

            Book book=convertToEntity(bookTO);
            return convertToTO(bookRepository.save(book));
        } catch (BadRequestException badRequestException){
            throw badRequestException;
        }catch (ConflictException conflictException){
            throw conflictException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    @Override
    public BookTO updateBook(Long id,BookTO bookTO) throws ResourceNotFoundException, ConflictException {
        try {
            Book oldBook= bookRepository.findById(id).
                    orElseThrow(()->new ResourceNotFoundException ("no Book found with id = "+id));
            if(bookRepository.findByIsbnForExistingBook(bookTO.getIsbn(),id)!=null){
                throw new ConflictException("Isbn already added before");
            }
            if(bookTO.getTitle()!=null || !bookTO.getTitle().trim().isEmpty()){
                oldBook.setTitle(bookTO.getTitle());
            }if(bookTO.getAuthor()!=null || !bookTO.getAuthor().trim().isEmpty()){
                oldBook.setAuthor(bookTO.getAuthor());
            }if(bookTO.getIsbn()!=null || !bookTO.getIsbn().trim().isEmpty()){
                oldBook.setIsbn(bookTO.getIsbn());
            }if(bookTO.getPublicationYear()>0 ){
                oldBook.setPublicationYear(bookTO.getPublicationYear());
            }
            bookRepository.save(oldBook);
            return convertToTO(bookRepository.save(oldBook));
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (ConflictException conflictException){
            throw conflictException;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    @Override
    public String removeBook(Long id) throws ResourceNotFoundException, ConflictException {
        try {
            Book oldBook= bookRepository.findById(id).
                    orElseThrow(()->new ResourceNotFoundException ("no Book found with id = "+id));
            int count=borrowingRecordRepository.getBorrowingRecordsCountByBookId(id);
            if(count==0){
                bookRepository.delete(oldBook);
                return "Book Deleted Successfully!";
            }else {
                throw new ConflictException("cannot remove book already borrowed");
            }
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (ConflictException conflictException){
            throw conflictException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }

    }

    //    private ModelMapper modelMapper;
    public BookTO convertToTO(Book book){
        BookTO bookTO=new BookTO();
        bookTO.setId(book.getId());
        bookTO.setAuthor(book.getAuthor());
        bookTO.setIsbn(book.getIsbn());
        bookTO.setTitle(book.getTitle());
        bookTO.setPublicationYear(book.getPublicationYear());
        return bookTO;
    }
    public Book convertToEntity(BookTO bookTO){
        Book book=new Book();
        book.setId(bookTO.getId());
        book.setAuthor(bookTO.getAuthor());
        book.setIsbn(bookTO.getIsbn());
        book.setTitle(bookTO.getTitle());
        book.setPublicationYear(bookTO.getPublicationYear());
        return book;
    }

}
