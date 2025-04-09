package com.allbib;

import com.allbib.entity.*;
import com.allbib.utils.gson.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BooksController implements Initializable {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    private User activeUser;
    private Book activeBook;
    private Review activeReview;
    private ObservableList<Book> bookTableList = FXCollections.observableArrayList();
    private ObservableList<Review> reviewList = FXCollections.observableArrayList();

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

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
    private Label availabilityDetail;

    @FXML
    private Label titleDetail2;

    @FXML
    private Label authorDetail2;

    @FXML
    private Label review;

    // Request
    @FXML
    private Label requestResponse;

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

    //for write review
    @FXML
    private TextArea reviewField;

    @FXML
    private Label submitResponse;

    @FXML
    public void submitReviewAction()
    {
        if (reviewField.getText().isEmpty())
            submitResponse.setText("Empty text area!");
        else {
            ExecutorService es = Executors.newCachedThreadPool();

            //transforming date to formatted string
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String sDate = formatter.format(date);

            //creating new review object
            Review newReview = new Review(reviewField.getText(), activeBook, activeUser.getUsername(), sDate);
            String command = "addReview";
            String payload = GsonUtil.getGson().toJson(newReview);

            System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
            SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

            Future<String> response = es.submit(commandWithSocket);
            try {
//                Blocking this thread until the server responds
                serverResponse = response.get();
                System.out.println("Response from server is : " + serverResponse);
                if (serverResponse.equals("Valid")) {
                    System.out.println("Review successfully added!");
                    submitResponse.setText("Review submitted");
                    reviewField.clear();
                }
                else
                    submitResponse.setText("Operation failed! Please try again!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //for all tabs
    @FXML
    public void clickOnTable()
    {
        activeBook = booksTable.getSelectionModel().getSelectedItem();
        setActiveBookDetails();
        System.out.println("active book id: " + booksTable.getSelectionModel().getSelectedItem().getIdBook());
        getReviewsFromServer();
        review.setText("");
        requestResponse.setText("");
    }

    @FXML
    private void refreshAction(){
        refreshBooksTable();
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
            //Book[] books = GsonUtil.getGson().fromJson(serverResponse,Book[].class);
            Type collectionType = new TypeToken<Collection<Book>>(){}.getType();
            Collection<Book> collection = GsonUtil.getGson().fromJson(serverResponse, collectionType);
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

    //for requests tab
    @FXML
    public void requestAction()
    {
        if (activeBook.getAvailability().equals("yes")) {
            LocalDate requestDate = LocalDate.now();
            //creating new request and push to the server
            Request newRequest = new Request (booksTable.getSelectionModel().getSelectedItem(), getActiveUser().getUsername(), requestDate);
            addRequestToServer(newRequest);

            //log request
            BookLog bookLog = new BookLog (activeBook, "Requested by: " + activeUser.getUsername(), requestDate);
            addBookLogToServer(bookLog);
        }
        else {
        requestResponse.setText("Not available right now");
        }
    }

    //for Request tab
    public void setActiveBookDetails(){
        titleDetail.setText(activeBook.getTitle());
        authorDetail.setText(activeBook.getAuthor());
        yearDetail.setText(activeBook.getYear());
        publisherDetail.setText(activeBook.getPublisher());
        pagesDetail.setText(Integer.toString(activeBook.getPages()));
        availabilityDetail.setText(activeBook.getAvailability());

        //for addReview tab
        titleDetail2.setText(activeBook.getTitle());
        authorDetail2.setText(activeBook.getAuthor());
    }

    //for Reviews tab
    @FXML
    public void clickOnReview()
    {
        activeReview = reviewsListView.getSelectionModel().getSelectedItem();
        setActiveReviewDetails();
        System.out.println("active review id: " + reviewsListView.getSelectionModel().getSelectedItem().getIdReview());
    }

    //for Reviews tab
    public void setActiveReviewDetails(){
        review.setText(activeReview.getReview());
    }

    //for Reviews tab
    //couldn't manage with template function :(
    private void getReviewsFromServer(){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "fetchReviewsForBook";
        String payload = GsonUtil.getGson().toJson(activeBook);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            //Review[] reviews = GsonUtil.getGson().fromJson(serverResponse,Review[].class);
            Type collectionType = new TypeToken<Collection<Review>>(){}.getType();
            Collection<Review> collection = GsonUtil.getGson().fromJson(serverResponse, collectionType);
            reviewList.clear();
            reviewList.addAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRequestToServer(Request newRequest){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "addRequest";
        String payload = GsonUtil.getGson().toJson(newRequest);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
//                Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("Request successfully added!");
                requestResponse.setText("Request submitted for this book");
            }
            else
                requestResponse.setText("Operation failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBookLogToServer(BookLog bookLog){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "addBookLog";
        String payload = GsonUtil.getGson().toJson(bookLog);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
//                Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("BookLog successfully added!");
            }
            else
                System.out.println("Adding BookLog failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshBooksTable(){
        getBooksFromServer();
        initCols();
        booksTable.setItems(bookTableList);
        if (!bookTableList.isEmpty()) {
            activeBook = bookTableList.get(0);
        }
        setActiveBookDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshBooksTable();

        getReviewsFromServer();
        System.out.println(reviewList);

        reviewsListView.setItems(reviewList);
        if (!reviewList.isEmpty()) {
            activeReview = reviewList.get(0);
        }
        setActiveReviewDetails();
    }
}
