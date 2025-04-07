package sample.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
//import javax.persistence.*;


/**
 * The persistent class for the books database table.
 * 
 */
//@Entity
//@Table(name="books")
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="id_book")
	private int idBook;

	private String title;

	private String author;

	private String publisher;

	private String year;

	private int pages;

	private String section;

	private String availability;

	//failed to lazily initialize a collection of role: com.testcompany.entity.Book.bookLogList, could not initialize proxy - no Session
//	@OneToMany(fetch = FetchType.EAGER)
//	@OneToMany
//	private Collection<BookLog> bookLogList;

	public Book() {
	}

	public Book(int idBook, String title, String author, String publisher, String year, int pages, String section, String availability, int idReader) {
		this.idBook = idBook;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.year = year;
		this.pages = pages;
		this.section = section;
		this.availability = availability;
	}

	public Book(String title, String author, String publisher, String year, int pages, String section, String availability) {
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.year = year;
		this.pages = pages;
		this.section = section;
		this.availability = availability;
	}

	public int getIdBook() {
		return idBook;
	}

	public void setIdBook(int idBook) {
		this.idBook = idBook;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

//	public Collection<BookLog> getBookLogList() {
//		return bookLogList;
//	}
//
//	public void setBookLogList(Collection<BookLog> bookLogList) {
//		this.bookLogList = bookLogList;
//	}

	@Override
	public String toString() {
		return "'" + title + '\'' +
				", by '" + author + '\'' +
				", publisher: '" + publisher + '\'' +
				", year: '" + year + '\'' +
				", pages: " + pages +
				", section: '" + section + '\'' +
				", availability: '" + availability + '\'' +
				'}';
	}
}