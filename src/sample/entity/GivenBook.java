package sample.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * The persistent class for the given_book database table.
 *
 */
@Entity
@Table(name="given_book")
public class GivenBook implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_given_book", unique = true, nullable = false)
    private Integer idGivenBook;

    @ManyToOne
    @JoinColumn(name="book_id_book")
    private Book book;

    private String username;

    @Column(name="approval_date")
    private LocalDate approvalDate;

    @Column(name="return_date")
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