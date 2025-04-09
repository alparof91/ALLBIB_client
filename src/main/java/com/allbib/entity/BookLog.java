package com.allbib.entity;

import java.io.Serializable;
import java.time.LocalDate;


public class BookLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idBookLog;
    private Book book;
    private String message;
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