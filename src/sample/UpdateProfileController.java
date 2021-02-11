package sample;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import sample.SocketClientCallable;
import sample.entity.Reader;
import sample.entity.User;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UpdateProfileController implements Initializable {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    public BorderPane editProfBordPane;

    @FXML
    public Label updateMessageLabel;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField secondNameField;

    @FXML
    public TextField addressField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneField;

    @FXML
    public TextField userField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public PasswordField repeatPasswordField;

    @FXML
    public void updateProfileAction() {
        validateUpdateInfo();
    }

    private void validateUpdateInfo() {

        if (firstNameField.getText().isEmpty() || secondNameField.getText().isEmpty() || addressField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || userField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("All the fields are needed!");
            updateMessageLabel.setText("There are empty fields! Try again!");
        } else if (!passwordField.getText().equals(repeatPasswordField.getText())) {
            updateMessageLabel.setText("Different password fields! Try again!");
            System.out.println("Password repeat error");
        } else {
            User user = new User(userField.getText(), passwordField.getText());
            String payload = new Gson().toJson(user);
            System.out.println("Sending to server:\ncommand: addUser,\ndata: " + payload);
            sendToServer("modifyUser", payload);

            Reader reader = new Reader(firstNameField.getText(), secondNameField.getText(), addressField.getText(), emailField.getText(), phoneField.getText(), user);
            payload = new Gson().toJson(reader);
            System.out.println("Sending to server:\ncommand: addReader,\ndata: " + payload);
            sendToServer("addReader", payload);

            System.out.println(userField.getText() + " updated his profile. Back to login!");
        }
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
            if (serverResponse.equals("Valid"))
                System.out.println("Successful operation!");
            else
                updateMessageLabel.setText("Operation failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
}