package com.allbib;

import com.allbib.entity.Admin;
import com.allbib.entity.User;
import com.allbib.utils.gson.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ManageAdminsController implements Initializable {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    private TableView<Admin> adminTable;

    @FXML
    private TableColumn<Admin, Integer> adminID;

    @FXML
    private TableColumn<Admin, String> adminFirstName;

    @FXML
    private TableColumn<Admin, String> adminSecondName;

    @FXML
    private TableColumn<Admin, String> adminAddress;

    @FXML
    private TableColumn<Admin, String> adminEmail;

    @FXML
    private TableColumn<Admin, String> adminTelephone;

    @FXML
    private TableColumn<Admin, User> adminUser;

    ObservableList<Admin> adminTableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String payload = "";
        String command = "fetchAdmins";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            //Admin[] admins = GsonUtil.getGson().fromJson(serverResponse,Admin[].class);
            Type collectionType = new TypeToken<Collection<Admin>>(){}.getType();
            Collection<Admin> admin = GsonUtil.getGson().fromJson(serverResponse, collectionType);
            adminTableList.addAll(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adminID.setCellValueFactory(new PropertyValueFactory<>("idAdmin"));
        adminFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        adminSecondName.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        adminAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        adminEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        adminTelephone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        adminUser.setCellValueFactory(new PropertyValueFactory<>("user"));

        adminTable.setItems(adminTableList);
    }
}
