package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entity.Book;
import sample.entity.BookLog;
import sample.entity.GivenBook;
import sample.entity.Notification;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReturnBookController implements Initializable {

    private GivenBook activeGivenBook;
    private Book activeBook;

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    // GivenBook Table
    // ------------------------------------------------
    @FXML
    private TableView<GivenBook> givenBooksTable;

    @FXML
    private TableColumn<GivenBook, Integer> idColumn;

    @FXML
    private TableColumn <GivenBook, Book> bookColumn;

    @FXML
    private TableColumn <GivenBook, String> usernameColumn;

    @FXML
    private TableColumn <GivenBook, String> approvalDateColumn;

    @FXML
    private TableColumn <GivenBook, String> returnDateColumn;

    ObservableList<GivenBook> givenBookTableList = FXCollections.observableArrayList();

    // GivenBook details
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
    private Label approvalDateDetail;

    @FXML
    private Label returnDateDetail;

    @FXML
    private Label returnBookRespond;

    // Notifications
    // ------------------------------------------------
    @FXML
    private Label userToNotify;

    @FXML
    private TextArea notificationArea;

    // Methods
    // ------------------------------------------------
    @FXML
    public void clickOnGivenBooks()
    {
        activeGivenBook = givenBooksTable.getSelectionModel().getSelectedItem();
        setActiveGivenBookDetails();
        activeBook=activeGivenBook.getBook();
        System.out.println("active review id: " + givenBooksTable.getSelectionModel().getSelectedItem().getIdGivenBook());
    }

    @FXML
    private void returnAction(){

        LocalDate effectiveReturnDate = LocalDate.now();
        BookLog bookLog = new BookLog (activeBook, "Book returned by: " + activeGivenBook.getUsername(), effectiveReturnDate);
        addBookLogToServer(bookLog);

        removeGivenBooksFromServer(activeGivenBook);
        refreshGivenBooksList();
        returnBookRespond.setText("Book returned!");

        //modify books availability an update on server
        activeBook.setAvailability("yes");
        updateBookOnServer(activeBook);
    }

    @FXML
    private void sendNotificationAction (){
        LocalDate notificationDate = LocalDate.now();
        Notification newNotification = new Notification(activeBook, notificationArea.getText(), activeGivenBook.getUsername(), notificationDate);
        addNotificationsToServer(newNotification);
        notificationArea.setText("");
    }

    public void setActiveGivenBookDetails(){
        titleDetail.setText(activeGivenBook.getBook().getTitle());
        authorDetail.setText(activeGivenBook.getBook().getAuthor());
        yearDetail.setText(activeGivenBook.getBook().getYear());
        userDetail.setText(activeGivenBook.getUsername());
        approvalDateDetail.setText(activeGivenBook.getApprovalDate().toString());
        returnDateDetail.setText(activeGivenBook.getReturnDate().toString());
        userToNotify.setText(activeGivenBook.getUsername());
    }

    private void getGivenBooksFromServer(){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String payload = "";
        String command = "fetchGivenBooks";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            Gson gson = new Gson();
//            GivenBook[] givenBooks = gson.fromJson(serverResponse,GivenBook[].class);
            Type collectionType = new TypeToken<Collection<GivenBook>>(){}.getType();
            Collection<GivenBook> givenBooks = gson.fromJson(serverResponse, collectionType);
            givenBookTableList.clear();
            givenBookTableList.addAll(givenBooks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeGivenBooksFromServer(GivenBook givenBookToRemove){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "removeGivenBook";
        String payload = new Gson().toJson(givenBookToRemove);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("GivenBook successfully deleted!");
            }
            else
                System.out.println("Deleting givenBook failed! Please try again!");
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

    private void addNotificationsToServer(Notification notification){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "addNotification";
        String payload = new Gson().toJson(notification);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("Notification successfully added!");
            }
            else
                System.out.println("Adding Notification failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void initCols(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idGivenBook"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("book"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        approvalDateColumn.setCellValueFactory(new PropertyValueFactory<>("approvalDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    @FXML
    private void refreshAction(){
        refreshGivenBooksList();
    }

    public void refreshGivenBooksList(){
        getGivenBooksFromServer();
        initCols();
        givenBooksTable.setItems(givenBookTableList);
        if (!givenBookTableList.isEmpty()) {
            activeGivenBook = givenBookTableList.get(0);
            setActiveGivenBookDetails();
            activeBook = activeGivenBook.getBook();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshGivenBooksList();
    }
}
