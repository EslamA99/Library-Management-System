package com.maids.librarymanagementsystem.repository;

import com.maids.librarymanagementsystem.entity.Book;
import com.maids.librarymanagementsystem.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron,Long> {

    @Query("select patron from Patron patron where patron.contactInfo=:contactInfo ")
    Patron findByContact(@Param("contactInfo")String contactInfo);

    @Query("select patron from Patron patron where patron.contactInfo=:contactInfo and patron.id<>:id ")
    Patron findByContactForExistingPatron(@Param("contactInfo")String contactInfo,@Param("id") Long id);


//    @Query("select p from Patron p where p.id=:id ")
//    Patron getById(@Param("id") Long id);
}
