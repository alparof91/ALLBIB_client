package com.allbib;

import com.allbib.entity.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginController{

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize(){
    }

    //for testing
    @FXML
    private void signUpButtonOnAction() {

        sceneSwitcher("Registration.fxml", 700, 500);
    }

    @FXML
    public void loginButtonOnAction(){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        User user = new User(userField.getText(), passwordField.getText(), "reader");
        String payload = new Gson().toJson(user);
        String command = "login";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse == null) {
                System.out.println("Not connected to server!");
                loginMessageLabel.setText("Not connected to server!");
            }
            else if (serverResponse.equals("admin")) {
                Stage adminStage =new Stage();
                stageSwitcher(adminStage, 1024, 768,"ALLBIB Administrator Window", "AdminWindow.fxml",  user);
                Stage stageTheLabelBelongs = (Stage) loginButton.getScene().getWindow();
                stageTheLabelBelongs.close();
            }
            else if (serverResponse.equals("reader")) {
                Stage adminStage =new Stage();
                stageSwitcher(adminStage, 1024, 768,"ALLBIB Desktop Application", "ReaderWindow.fxml",  user);
                Stage stageTheLabelBelongs = (Stage) loginButton.getScene().getWindow();
                stageTheLabelBelongs.close();
            }
            else {
                loginMessageLabel.setText("Invalid credentials! Try again!");
                System.out.println("Login response: " + serverResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sceneSwitcher(String fileNameFXML, int width, int height) {
        //change scene to employeeWindowParent
        Stage stageTheLabelBelongs = (Stage) loginButton.getScene().getWindow();
        try {
            Parent employeeWindowParent = FXMLLoader.load(getClass().getResource(fileNameFXML));
            stageTheLabelBelongs.setScene(new Scene(employeeWindowParent, width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stageSwitcher(Stage stage, int width, int height, String title, String fileNameFXML, User activeUser) {
        stage.setTitle(title);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileNameFXML));
            Parent windowParent = loader.load();
            if (fileNameFXML.equals("AdminWindow.fxml")) {
                AdminWindowController adminWindowController = loader.getController();
                adminWindowController.setUser(activeUser);
            }
            else{
                ReaderWindowController readerWindowController = loader.getController();
                readerWindowController.setUser(activeUser);
            }

            stage.setScene(new Scene(windowParent, width, height));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
