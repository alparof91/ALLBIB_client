package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entity.Readers;
import sample.entity.User;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ManageReadersController {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    private TableView<Readers> readersTable;

    @FXML
    private TableColumn<Readers, Integer> readerID;

    @FXML
    private TableColumn<Readers, String> readerFirstName;

    @FXML
    private TableColumn<Readers, String> readerSecondName;

    @FXML
    private TableColumn<Readers, String> readerAddress;

    @FXML
    private TableColumn<Readers, String> readerEmail;

    @FXML
    private TableColumn<Readers, String> readerTelephone;

    @FXML
    private TableColumn<Readers, User> readerUser;

    ObservableList<Readers> readersTableList = FXCollections.observableArrayList();

    public void initializeDisplayReadersController(){

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
            //Readers[] readers = gson.fromJson(serverResponse,Readers[].class);
            Type collectionType = new TypeToken<Collection<Readers>>(){}.getType();
            Collection<Readers> readers = gson.fromJson(serverResponse, collectionType);
            readersTableList.addAll(readers);
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

        readersTable.setItems(readersTableList);
    }
}
