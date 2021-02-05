package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.entity.User;

import java.io.IOException;

public class AdminWindowController {

    @FXML
    private BorderPane displayProfilePane;

    @FXML
    private AnchorPane addBookSplitPaneDown;

    @FXML
    private AnchorPane displayBooksPane;

    @FXML
    private AnchorPane addBookPane;

    @FXML
    private Button updateProfileButton;

    //Add New Book
    @FXML
    private TextField addBookTitle;

    //Add New Book
    @FXML
    private TextField addBookAuthor;

    //Add New Book
    @FXML
    private TextField addBookPublisher;

    //Add New Book
    @FXML
    private TextField addBookYear;

    //Add New Book
    @FXML
    private TextField addBookPages;

    //Add New Book
    @FXML
    private TextField addBookSection;

    //Add New Book
    @FXML
    private Button addBookButton;

    //Display Readers
    @FXML
    private AnchorPane displayReadersPane;

    //Add New Reader
    @FXML
    private AnchorPane addReaderPane;

    @FXML
    private Label userLabel;

    @FXML
    private Hyperlink exitButton;

    @FXML
    public void editYourProfileAction(){
        displayProfilePane.toFront();
    }

    @FXML
    public void addBookAction() {
//        manageBooksController.initializeDisplayBooksController();
        addBookPane.toFront();
    }

    @FXML
    public void addReaderAction() {
//        manageReadersController.initializeDisplayReadersController();
        addReaderPane.toFront();
    }

    @FXML
    public void manageAdministratorAction() {
        addReaderPane.toFront();
        try {
            Node displayReadersPaneChildren = FXMLLoader.load(getClass().getResource("ManageReaders.fxml"));
//            addBookSplitPaneDown.getChildren().setAll(displayReadersPaneChildren);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void displayReviewsAction() {
        addReaderPane.toFront();
//        try {
//            Node displayReadersPaneChildren = FXMLLoader.load(getClass().getResource("ManageBooks.fxml"));
//            addBookSplitPaneDown.getChildren().setAll(displayReadersPaneChildren);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        Stage stageTheLabelBelongs = (Stage) exitButton.getScene().getWindow();
        stageTheLabelBelongs.close();
    }

    @FXML
    public void exitAction() {
        Stage stageTheLabelBelongs = (Stage) exitButton.getScene().getWindow();
        stageTheLabelBelongs.close();
        System.exit(0);
    }

    public void getInfo(String username) {
        userLabel.setText(username);
    }
}
