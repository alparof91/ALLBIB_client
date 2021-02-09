package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sample.entity.Books;
import sample.entity.User;

import java.lang.reflect.Type;
import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AdminWindowController implements Initializable {
//public class AdminWindowController {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    private Pane displayBooksPane;

    @FXML
    private Pane addBookPane;

    @FXML
    private Pane displayReadersPane;

    @FXML
    private TableView <Books> booksTable;

    @FXML
    private TableColumn <Books, Integer> idColumn;

    @FXML
    private TableColumn <Books, String> titleColumn;

    @FXML
    private TableColumn <Books, String> authorColumn;

    @FXML
    private TableColumn <Books, String> publisherColumn;

    @FXML
    private TableColumn <Books, String> yearColumn;

    @FXML
    private TableColumn <Books, Integer> pagesColumn;

    @FXML
    private TableColumn <Books, String> sectionColumn;

    @FXML
    private TableColumn <Books, String> availabilityColumn;

    @FXML
    private TableColumn <Books, Integer> atReaderColumn;

    @FXML
    private TableColumn <Books, Date> untilColumn;

    ObservableList<Books> booksTableList = FXCollections.observableArrayList();

    @FXML
    public void booksButtonOnAction(ActionEvent event){
        displayBooksPane.toFront();

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
            Type collectionType = new TypeToken<Collection<Books>>(){}.getType();
            Collection<Books> books = gson.fromJson(serverResponse, collectionType);
            Iterator<Books> iterator = books.iterator();
            while (iterator.hasNext()) {
                booksTableList.add(iterator.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection to server terminated");
    }

    @FXML
    public void addBookOnAction(ActionEvent event){
        addBookPane.toFront();

        System.out.println("You have successfully signed up! Back to login!");
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        Books books = new Books(4, "Head First Java (A Brain Friendly Guide)", "Kathy Sierra, Bert Bates", "O Reilly", "2005", 500, "B/243/CD", "yes", 0, "none");
        String payload = new Gson().toJson(books);
        String command = "addBook";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            if (serverResponse.equals("Valid"))
                System.out.println("Book successfully added!");
            else
                System.out.println("Book has not been added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection to server terminated");
    }

    ObservableList<Books> readersTableList = FXCollections.observableArrayList();

    @FXML
    public void readersButtonOnAction(ActionEvent event){
        displayReadersPane.toFront();

        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String payload = "";
        String command = "fetchReaders";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            Gson gson = new Gson();
            //Reader[] readers = gson.fromJson(serverResponse,Book[].class);
            Type collectionType = new TypeToken<Collection<Books>>(){}.getType();
            Collection<Books> books = gson.fromJson(serverResponse, collectionType);
            Iterator<Books> iterator = books.iterator();
            while (iterator.hasNext()) {
                readersTableList.add(iterator.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection to server terminated");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        displayBooksPane.toFront();
        idColumn.setCellValueFactory(new PropertyValueFactory<Books, Integer>("idBook"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("publisher"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("year"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<Books, Integer>("pages"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("section"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("availability"));
        atReaderColumn.setCellValueFactory(new PropertyValueFactory<Books, Integer>("idReader"));
        untilColumn.setCellValueFactory(new PropertyValueFactory<Books, Date>("returnDate"));

        booksTable.setItems(booksTableList);
    }
}
