package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.entity.Books;

import java.lang.reflect.Type;
import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//public class ManageBooksController implements Initializable {
public class ManageBooksController {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

//    @FXML
//    private AdminWindowController adminWindowController;

    @FXML
    private TableView<Books> booksTable;

    @FXML
    private TableColumn<Books, Integer> idColumn;

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

    @FXML
    private TableColumn <Books, Button> deleteColumn;

    ObservableList<Books> booksTableList = FXCollections.observableArrayList();

    public  void initializeDisplayBooksController(){
        getDataFromServer();
        initCols();
        booksTable.setItems(booksTableList);
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
            //Books[] books = gson.fromJson(serverResponse,Books[].class);
            Type collectionType = new TypeToken<Collection<Books>>(){}.getType();
            Collection<Books> books = gson.fromJson(serverResponse, collectionType);
            booksTableList.addAll(books);
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
}
