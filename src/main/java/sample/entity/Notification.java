package sample.entity;

//import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * The persistent class for the notification database table.
 *
 */
//@Entity
//@Table(name="notification")
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="id_notification", unique = true, nullable = false)
    private Integer idNotification;

//    @ManyToOne
//    @JoinColumn(name="book_id_book")
    private Book book;

    private String message;

    private String username;

//    @Column(name="date")
    private LocalDate date;

    public Notification() {
    }

    public Notification(Book book, String message, String username, LocalDate date) {
        this.book = book;
        this.message = message;
        this.username = username;
        this.date = date;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Integer idNotification) {
        this.idNotification = idNotification;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "idNotification=" + idNotification +
                ", book=" + book +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", date=" + date +
                '}';
    }
}