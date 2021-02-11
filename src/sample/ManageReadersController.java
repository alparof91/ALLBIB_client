package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.SocketClientCallable;
import sample.entity.Reader;
import sample.entity.User;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ManageReadersController implements Initializable {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    private TableView<Reader> readersTable;

    @FXML
    private TableColumn<Reader, Integer> readerID;

    @FXML
    private TableColumn<Reader, String> readerFirstName;

    @FXML
    private TableColumn<Reader, String> readerSecondName;

    @FXML
    private TableColumn<Reader, String> readerAddress;

    @FXML
    private TableColumn<Reader, String> readerEmail;

    @FXML
    private TableColumn<Reader, String> readerTelephone;

    @FXML
    private TableColumn<Reader, User> readerUser;

    ObservableList<Reader> readerTableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
            //Reader[] readers = gson.fromJson(serverResponse,Reader[].class);
            Type collectionType = new TypeToken<Collection<Reader>>(){}.getType();
            Collection<Reader> readers = gson.fromJson(serverResponse, collectionType);
            readerTableList.addAll(readers);
        } catch (Exception e) {
            e.printStackTrace();
        }

        readerID.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        readerFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        readerSecondName.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        readerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        readerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        readerTelephone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        readerUser.setCellValueFactory(new PropertyValueFactory<>("user"));

        readersTable.setItems(readerTableList);
    }
}
