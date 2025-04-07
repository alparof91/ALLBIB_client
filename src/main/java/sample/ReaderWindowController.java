package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.entity.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReaderWindowController implements Initializable {

    private User user;

    public void setUser(User user) {
        this.user = user;
        userLabel.setText(user.getUsername());
    }

    @FXML
    private BorderPane updateProfile;

    @FXML
    private AnchorPane center;

    @FXML
    private Label userLabel;

    @FXML
    private Hyperlink signOutButton;

    @FXML
    private Hyperlink exitButton;

    @FXML
    public void editYourProfileAction(){
        updateProfile.toFront();
    }

    @FXML
    public void notificationsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MyNotifications.fxml"));
            AnchorPane notifications = loader.load();
            center.getChildren().add(notifications);
            MyNotificationsController myNotificationsController = loader.getController();
//            myNotificationsController.setActiveUser(user);
            myNotificationsController.loadNotificationWithUser(user);
            notifications.toFront();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void manageBooksAction() {
        //for passing activeUser to the BooksController
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Books.fxml"));
            AnchorPane books = loader.load();
            center.getChildren().add(books);
            BooksController booksController = loader.getController();
            booksController.setActiveUser(user);
            books.toFront();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signOutAction() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Welcome to ALLBIB");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent windowParent = loader.load();
            loginStage.setScene(new Scene(windowParent, 700, 400));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stageTheLabelBelongs = (Stage) signOutButton.getScene().getWindow();
        stageTheLabelBelongs.close();
    }

    @FXML
    public void exitAction() {
        Stage stageTheLabelBelongs = (Stage) exitButton.getScene().getWindow();
        stageTheLabelBelongs.close();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editYourProfileAction();
    }
}
