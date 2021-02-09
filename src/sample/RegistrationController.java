package sample;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.entity.Reader;
import sample.entity.User;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegistrationController {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField birthdayField;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private Label signUpMessageLabel;

    @FXML
    private Button signUpButton;

    @FXML
    private void backToLogin () {
        System.out.println("You clicked Back!");
        Stage stageTheLabelBelongs = (Stage) signUpButton.getScene().getWindow();
        try {
            Parent signUpParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
            stageTheLabelBelongs.setScene(new Scene(signUpParent, 700, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void validateSignUp(){

        if (firstNameField.getText().isEmpty() || secondNameField.getText().isEmpty() || addressField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || birthdayField.getText().isEmpty() || userField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("All the fields are needed!");
            signUpMessageLabel.setText("All the fields have to be completed! Try again!");
        }
        else if (!passwordField.getText().equals(passwordField2.getText())) {
            signUpMessageLabel.setText("The content of the password fields are different! Try again!");
            System.out.println("Password repeat error");
        }
        else {
            User user = new User(userField.getText(), passwordField.getText());
            String payload = new Gson().toJson(user);
            System.out.println("Sending to server:\ncommand: addUser,\ndata: " + payload);
            sendToServer("addUser",payload);

            Reader reader = new Reader(firstNameField.getText(), secondNameField.getText(), addressField.getText(), emailField.getText(), phoneField.getText(), user);
            payload = new Gson().toJson(reader);
            System.out.println("Sending to server:\ncommand: addReader,\ndata: " + payload);
            sendToServer("addReader",payload);

            System.out.println(userField.getText() + "signed up! Back to login!");
            backToLogin();
        }
    }

    private void sendToServer(String command, String payload){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);
        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid"))
                System.out.println("Successful operation!");
            else
                signUpMessageLabel.setText("Operation failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
