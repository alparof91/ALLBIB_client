package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

//public class LoginController implements Initializable {
public class LoginController {

    ObservableList<String> titleList = FXCollections.observableArrayList("Employee","Reader");

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox titleChoiceBox;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private void initialize(){
        titleChoiceBox.setItems(titleList);
        titleChoiceBox.setValue("Reader");
    }

//    ??????
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle){
//        public void initialize(URL url, ResourceBundle resourceBundle){
//        File brandingFile = new File("images/ALLBIB.png");
//        Image brandingImage = new Image(brandingFile.toURI().toString());
//        brandingImageView.setImage(brandingImage);
//    };

    public void loginButtonOnAction(ActionEvent event){
        if (userField.getText().isEmpty() == false && passwordField.getText().isEmpty() == false)
            validateLogin();
        else
            loginMessageLabel.setText("User or Password field is empty!");
    }

    public void validateLogin(){
        loginMessageLabel.setText("Oops! Wrong User or Password!");
    }

}
