package sample.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the review database table.
 *
 */
@Entity
@Table(name="reviews")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_reviews")
	private Integer idReview;

	private String review;

	@ManyToOne
	@Column(name="books_id_book")
	private Book book;

	public Review() {
	}

	public Review(Integer idReview, String review, Book book) {
		this.idReview = idReview;
		this.review = review;
		this.book = book;
	}

	public Integer getIdReview() {
		return idReview;
	}

	public void setIdReview(Integer idReview) {
		this.idReview = idReview;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Book getBooks() {
		return book;
	}

	public void setBooks(Book book) {
		this.book = book;
	}

	@Override
	public String toString() {
		return idReview + review;

	}
}