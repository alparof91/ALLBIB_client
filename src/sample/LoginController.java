package sample;

import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginController{

    ObservableList<String> titleList = FXCollections.observableArrayList("Admin","Reader");

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> titleChoiceBox;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize(){
        titleChoiceBox.setItems(titleList);
        titleChoiceBox.setValue("Reader");
    }

    //for testing
    @FXML
    private void signUpButtonOnAction() {

        sceneSwitcher("Registration.fxml", 700, 500);
    }

    @FXML
    public void loginButtonOnAction(){
        User user = new User("raduvasile", "123456");
        Stage adminStage = new Stage();
        stageSwitcher(adminStage, 1024, 768,"ALLBIB Administrator Window", "AdminWindow.fxml",  user.getUsername());
        Stage stageTheLabelBelongs = (Stage) loginButton.getScene().getWindow();
        stageTheLabelBelongs.close();


    }

//    @FXML
//    public void loginButtonOnAction(){
//        //based on server_client_example
//        ExecutorService es = Executors.newCachedThreadPool();
//
//        User user = new User(userField.getText(), passwordField.getText());
//        String payload = new Gson().toJson(user);
//        String command = "login";
//
//        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
//        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);
//
//        Future<String> response = es.submit(commandWithSocket);
//        try {
//            // Blocking this thread until the server responds
//            serverResponse = response.get();
//            System.out.println("Response from server is : " + serverResponse);
//            if (serverResponse.equals("Valid credentials")) {
//                switch (titleChoiceBox.getValue()) {
//                    case "Admin": {
//                        Stage adminStage =new Stage();
//                        stageSwitcher(adminStage, "ALLBIB Administrator Window", "AdminWindow.fxml", 1024, 768);
//                        Stage stageTheLabelBelongs = (Stage) loginButton.getScene().getWindow();
//                        stageTheLabelBelongs.close();
//                        break;
//                    }
//                    case "Reader": {
//                        Stage adminStage =new Stage();
//                        stageSwitcher(adminStage, "ALLBIB Desktop Application", "ReaderWindow.fxml", 1024, 768);
//                        Stage stageTheLabelBelongs = (Stage) loginButton.getScene().getWindow();
//                        stageTheLabelBelongs.close();
//                        break;
//                    }
//                }
//            }
//            else {
//                loginMessageLabel.setText("Invalid credentials! Try again!");
//                System.out.println("Invalid credentials! Try again!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

    private void stageSwitcher(Stage stage, int width, int height, String title, String fileNameFXML, String username) {
        stage.setTitle(title);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileNameFXML));
            Parent windowParent = loader.load();
            AdminWindowController adminWindowController = loader.getController();
            adminWindowController.setUser(username);
            stage.setScene(new Scene(windowParent, width, height));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
