package com.allbib.entity;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * The persistent class for the given_book database table.
 *
 */
public class GivenBook implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idGivenBook;
    private Book book;
    private String username;
    private LocalDate approvalDate;
    private LocalDate returnDate;

    public GivenBook() {
    }

    public GivenBook(Book book, String username, LocalDate approvalDate, LocalDate returnDate) {
        this.book = book;
        this.username = username;
        this.approvalDate = approvalDate;
        this.returnDate = returnDate;
    }

    public Integer getIdGivenBook() {
        return idGivenBook;
    }

    public void setIdGivenBook(Integer idGivenBook) {
        this.idGivenBook = idGivenBook;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return  "[" + approvalDate +
                "] - [" + returnDate +
                "] User: " + username +
                " " + book;
    }
}