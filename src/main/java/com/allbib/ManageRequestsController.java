package com.allbib;

import com.allbib.entity.Book;
import com.allbib.entity.BookLog;
import com.allbib.entity.GivenBook;
import com.allbib.entity.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ManageRequestsController implements Initializable {

    private Request activeRequest;
    private Book activeBook;

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    // Request Table
    // ------------------------------------------------
    @FXML
    private TableView<Request> requestsTable;

    @FXML
    private TableColumn<Request, Integer> idColumn;

    @FXML
    private TableColumn <Request, Book> bookColumn;

    @FXML
    private TableColumn <Request, String> usernameColumn;

    @FXML
    private TableColumn <Request, String> requestDateColumn;

    ObservableList<Request> requestTableList = FXCollections.observableArrayList();

    // Request details
    // ------------------------------------------------

    @FXML
    private Label titleDetail;

    @FXML
    private Label authorDetail;

    @FXML
    private Label yearDetail;

    @FXML
    private Label userDetail;

    @FXML
    private Label requestDateDetail;

    @FXML
    private TextField allocatedDays;

    @FXML
    private Label dateFormatRespond;

    // Methods
    // ------------------------------------------------
    @FXML
    private void acceptAction(){
        //getting allocated days
        int days;
        if (isParsable(allocatedDays.getText()))
            days = Integer.parseInt(allocatedDays.getText());
        else {
            days = 10;
            dateFormatRespond.setText("Date format not accepted");
        }

        //modify books availability an update on server
        activeBook.setAvailability("no");
        updateBookOnServer(activeBook);
        removeRequestFromServer(activeRequest);

        LocalDate acceptDate = LocalDate.now();
        System.out.println("From:" + acceptDate + " - Until:" + acceptDate.plusDays(days));
        BookLog bookLog = new BookLog (activeBook, "Request accepted for: " + activeRequest.getUsername() , acceptDate);
        addBookLogToServer(bookLog);
        GivenBook givenBook = new GivenBook (activeBook, activeRequest.getUsername(), acceptDate, acceptDate.plusDays(10));
        addGivenBookToServer(givenBook);
        refreshRequestsList();
    }

    //from https://stackoverflow.com/questions/6456219/java-checking-if-parseint-throws-exception
    //to check if a field is parsable to Int
    public static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    @FXML
    private void declineAction(){
        LocalDate declineDate = LocalDate.now();
        BookLog bookLog = new BookLog (activeBook, "Request declined for: " + activeRequest.getUsername() , declineDate);
        addBookLogToServer(bookLog);
        removeRequestFromServer(activeRequest);
        refreshRequestsList();
    }

    @FXML
    private void refreshAction(){
        refreshRequestsList();
    }

    @FXML
    public void clickOnRequests()
    {
        activeRequest = requestsTable.getSelectionModel().getSelectedItem();
        setActiveRequestDetails();
        activeBook=activeRequest.getBook();
        System.out.println("active review id: " + requestsTable.getSelectionModel().getSelectedItem().getIdRequest());
    }

    public void setActiveRequestDetails(){
        titleDetail.setText(activeRequest.getBook().getTitle());
        authorDetail.setText(activeRequest.getBook().getAuthor());
        yearDetail.setText(activeRequest.getBook().getYear());
        userDetail.setText(activeRequest.getUsername());
        requestDateDetail.setText(activeRequest.getRequestDate().toString());
    }

    private void getRequestsFromServer(){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String payload = "";
        String command = "fetchRequests";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            Gson gson = new Gson();
//            Request[] requests = gson.fromJson(serverResponse,Request[].class);
            Type collectionType = new TypeToken<Collection<Request>>(){}.getType();
            Collection<Request> requests = gson.fromJson(serverResponse, collectionType);
            requestTableList.clear();
            requestTableList.addAll(requests);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCols(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idRequest"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("book"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    }

    private void updateBookOnServer(Book book){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "updateBook";
        String payload = new Gson().toJson(book);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("Book successfully modified!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeRequestFromServer(Request requestToRemove){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "removeRequest";
        String payload = new Gson().toJson(requestToRemove);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
//                Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("Request successfully deleted!");
            }
            else
                System.out.println("Deleting request failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshRequestsList(){
        getRequestsFromServer();
        initCols();
        requestsTable.setItems(requestTableList);
        if (!requestTableList.isEmpty()) {
            activeRequest = requestTableList.get(0);
            setActiveRequestDetails();
            activeBook = activeRequest.getBook();
        }
    }

    private void addGivenBookToServer(GivenBook givenBook){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "addGivenBook";
        String payload = new Gson().toJson(givenBook);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
//                Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("GivenBook successfully added!");
            }
            else
                System.out.println("Adding GivenBook failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBookLogToServer(BookLog bookLog){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "addBookLog";
        String payload = new Gson().toJson(bookLog);

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



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshRequestsList();
    }
}
