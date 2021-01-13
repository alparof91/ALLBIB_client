package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminWindowController implements Initializable{
//public class AdminWindowController {

    @FXML
    ManageBooksController manageBooksController;

    @FXML
    ManageReadersController manageReadersController;

    @FXML
    private AnchorPane displayProfilePane;

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
    public void addAdministratorAction() {
        addReaderPane.toFront();
        try {
            Node displayReadersPaneChildren = FXMLLoader.load(getClass().getResource("ManageBooks.fxml"));
            addBookSplitPaneDown.getChildren().setAll(displayReadersPaneChildren);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void displayReviewsAction() {
        addReaderPane.toFront();
        try {
            Node displayReadersPaneChildren = FXMLLoader.load(getClass().getResource("ManageBooks.fxml"));
            addBookSplitPaneDown.getChildren().setAll(displayReadersPaneChildren);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signOutAction() {
        addReaderPane.toFront();
        try {
            Node displayReadersPaneChildren = FXMLLoader.load(getClass().getResource("ManageBooks.fxml"));
            addBookSplitPaneDown.getChildren().setAll(displayReadersPaneChildren);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exitAction() {
        addReaderPane.toFront();
        try {
            Node displayReadersPaneChildren = FXMLLoader.load(getClass().getResource("ManageBooks.fxml"));
            addBookSplitPaneDown.getChildren().setAll(displayReadersPaneChildren);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void getInfo(String username) {
        userLabel.setText(username);
//        System.out.println(username);
    }
}
