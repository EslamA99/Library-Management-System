package com.maids.librarymanagementsystem.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PATRON")
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME",nullable = false)
    private String name;

    @Column(name = "CONTACT_INFO",unique = true,nullable = false)
    private String contactInfo;

    @OneToMany(mappedBy = "patron",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<BorrowingRecord>borrowingRecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<BorrowingRecord> getBorrowingRecords() {
        return borrowingRecords;
    }

    public void setBorrowingRecords(List<BorrowingRecord> borrowingRecords) {
        this.borrowingRecords = borrowingRecords;
    }
}
