package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entity.Book;
import sample.entity.Review;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BooksController implements Initializable {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    public Book activeBook;
    public Review activeReview;
    public ObservableList<Book> bookTableList = FXCollections.observableArrayList();
    public ObservableList<Review> reviewList = FXCollections.observableArrayList();

    // Info Labels
    // ------------------------------------------------
    @FXML
    private Label titleDetail;

    @FXML
    private Label authorDetail;

    @FXML
    private Label yearDetail;

    @FXML
    private Label publisherDetail;

    @FXML
    private Label pagesDetail;

    @FXML
    private Label titleDetail2;

    @FXML
    private Label authorDetail2;

    @FXML
    private Label review;

    // Book Table
    // ------------------------------------------------
    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, Integer> idColumn;

    @FXML
    private TableColumn <Book, String> titleColumn;

    @FXML
    private TableColumn <Book, String> authorColumn;

    @FXML
    private TableColumn <Book, String> publisherColumn;

    @FXML
    private TableColumn <Book, String> yearColumn;

    @FXML
    private TableColumn <Book, Integer> pagesColumn;

    @FXML
    private TableColumn <Book, String> sectionColumn;

    @FXML
    private TableColumn <Book, String> availabilityColumn;

    @FXML
    private ListView <Review> reviewsListView;

    //for all tabs
    @FXML
    public void clickOnTable()
    {
        activeBook = booksTable.getSelectionModel().getSelectedItem();
        setActiveBookDetails();
        System.out.println("active book id: " + booksTable.getSelectionModel().getSelectedItem().getIdBook());
        getReviewsFromServer();
    }

    //for all tabs
    //couldn't manage with template function :(
    private void getBooksFromServer(){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "fetchBooks";
        String payload = "";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            Gson gson = new Gson();
            //Book[] books = gson.fromJson(serverResponse,Book[].class);
            Type collectionType = new TypeToken<Collection<Book>>(){}.getType();
            Collection<Book> collection = gson.fromJson(serverResponse, collectionType);
            bookTableList.clear();
            bookTableList.addAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //for all tabs
    private void initCols(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idBook"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<>("pages"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    //for request tabs
    @FXML
    public void requestAction()
    {
//        activeBook = booksTable.getSelectionModel().getSelectedItem();
//        setActiveBookDetails();
        System.out.println("active book id: " + booksTable.getSelectionModel().getSelectedItem().getIdBook());
//        getReviewsFromServer();
    }

    //for Request tab
    public void setActiveBookDetails(){
        titleDetail.setText(activeBook.getTitle());
        authorDetail.setText(activeBook.getAuthor());
        yearDetail.setText(activeBook.getYear());
        publisherDetail.setText(activeBook.getPublisher());
        pagesDetail.setText(Integer.toString(activeBook.getPages()));

        //for add Review tab
        titleDetail2.setText(activeBook.getTitle());
        authorDetail2.setText(activeBook.getAuthor());
    }

    //for Review tab
    @FXML
    public void clickOnReview()
    {
        activeReview = reviewsListView.getSelectionModel().getSelectedItem();
        setActiveReviewDetails();
        System.out.println("active review id: " + reviewsListView.getSelectionModel().getSelectedItem().getIdReview());
    }

    //for Review tab
    public void setActiveReviewDetails(){
        review.setText(activeReview.getReview());
    }

    //for Review tab
    //couldn't manage with template function :(
    private void getReviewsFromServer(){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "fetchReviewsForBook";
        String payload = new Gson().toJson(activeBook);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            Gson gson = new Gson();
            //Review[] reviews = gson.fromJson(serverResponse,Review[].class);
            Type collectionType = new TypeToken<Collection<Review>>(){}.getType();
            Collection<Review> collection = gson.fromJson(serverResponse, collectionType);
            reviewList.clear();
            reviewList.addAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getBooksFromServer();
        initCols();
        booksTable.setItems(bookTableList);
        activeBook = bookTableList.get(0);
        setActiveBookDetails();

        getReviewsFromServer();
        System.out.println(reviewList);

        reviewsListView.setItems(reviewList);
        activeReview = reviewList.get(0);
        setActiveReviewDetails();
    }
}
