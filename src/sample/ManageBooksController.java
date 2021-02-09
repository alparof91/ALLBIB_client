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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entity.Book;

import java.lang.reflect.Type;
import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ManageBooksController implements Initializable {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    // Add New Book
    // ------------------------------------------------
    @FXML
    private TextField addBookTitle;

    @FXML
    private TextField addBookAuthor;

    @FXML
    private TextField addBookPublisher;

    @FXML
    private TextField addBookYear;

    @FXML
    private TextField addBookPages;

    @FXML
    private TextField addBookSection;

    @FXML
    private Label addMessageLabel;

    // Remove Book by ID
    // ------------------------------------------------

    @FXML
    private TextField removeBookId;

    // Table
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
    private TableColumn <Book, Integer> atReaderColumn;

    @FXML
    private TableColumn <Book, Date> untilColumn;

    ObservableList<Book> bookTableList = FXCollections.observableArrayList();

    @FXML
    private void addBookButtonAction(){
        if (addBookTitle.getText().isEmpty() || addBookAuthor.getText().isEmpty() || addBookPublisher.getText().isEmpty() || addBookYear.getText().isEmpty() || addBookPages.getText().isEmpty() || addBookSection.getText().isEmpty()) {
            System.out.println("All the fields are needed!");
            addMessageLabel.setText("There are empty fields! Try again!");
        } else {
            Book book = new Book(addBookTitle.getText(),addBookAuthor.getText(),addBookPublisher.getText(),addBookYear.getText(),Integer.parseInt(addBookPages.getText()),addBookSection.getText(),"yes");
            String payload = new Gson().toJson(book);
            System.out.println("Adding new book to database...");
            sendToServer("addBook", payload);
            getDataFromServer();
            booksTable.setItems(bookTableList);
        }
    }

    @FXML
    private void removeBookButtonAction(){
        sendToServer("removeBook", removeBookId.getText());
        System.out.println("Deleting book with ID:" + removeBookId.getText());
        getDataFromServer();
        booksTable.setItems(bookTableList);
    }

    private void sendToServer(String command, String payload) {
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);
        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("Successful operation!");
                addMessageLabel.setText("Book successfully added!");
            }
            else
                addMessageLabel.setText("Operation failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromServer(){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String payload = "";
        String command = "fetchBooks";

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
            Collection<Book> books = gson.fromJson(serverResponse, collectionType);
            bookTableList.clear();
            bookTableList.addAll(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCols(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idBook"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<>("pages"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        atReaderColumn.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        untilColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getDataFromServer();
        initCols();
        booksTable.setItems(bookTableList);
    }
}
