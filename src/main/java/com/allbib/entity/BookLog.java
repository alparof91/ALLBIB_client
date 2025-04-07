package com.allbib.entity;

//import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * The persistent class for the book_log database table.
 *
 */
//@Entity
//@Table(name="book_log")
public class BookLog implements Serializable {
    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="id_book_log", unique = true, nullable = false)
    private Integer idBookLog;

//    @ManyToOne
//    @JoinColumn(name="book_id_book")
    private Book book;

    private String message;

//    @Column(name="log_date")
    private LocalDate logDate;

    public BookLog() {
    }

    public BookLog(Book book, String message, LocalDate logDate) {
        this.book = book;
        this.message = message;
        this.logDate = logDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdBookLog() {
        return idBookLog;
    }

    public void setIdBookLog(Integer idBookLog) {
        this.idBookLog = idBookLog;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    @Override
    public String toString() {
        return  "[" + logDate +
                "] - " + message;
    }
}