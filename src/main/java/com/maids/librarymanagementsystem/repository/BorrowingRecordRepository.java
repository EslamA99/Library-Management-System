package com.maids.librarymanagementsystem.repository;

import com.maids.librarymanagementsystem.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {

    @Query("select count (br) from BorrowingRecord br where br.book.id=:bookId and br.returnDate is NULL ")
    int getBorrowingRecordsCountByBookId(@Param("bookId") Long bookId);

    @Query("select count (br) from BorrowingRecord br where br.patron.id=:patronId and br.returnDate is NULL ")
    int getBorrowingRecordsCountByPatronId(@Param("patronId") Long patronId);

    @Query("select br from BorrowingRecord br where br.book.id=:bookId and br.patron.id=:patronId")
    BorrowingRecord findByBookIdAndPatronId(@Param("bookId") Long bookId,@Param("patronId") Long patronId);
}
