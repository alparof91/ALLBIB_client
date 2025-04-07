package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entity.Notification;
import sample.entity.User;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static sample.predicates.NotificationPredicates.filterNotifications;
import static sample.predicates.NotificationPredicates.isUser;

public class MyNotificationsController implements Initializable {

    public static String serverResponse;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9001;

    private User activeUser;
    private Notification activeNotification;
    private ObservableList<Notification> notificationList = FXCollections.observableArrayList();

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public User getActiveUser() {
        return activeUser;
    }

    // Info Labels
    // ------------------------------------------------
    @FXML
    private Label dateDetail;

    @FXML
    private Label bookDetail;

    @FXML
    private Label messageDetail;

    // Notifications Table
    // ------------------------------------------------
    @FXML
    private TableView<Notification> notificationsTable;

    @FXML
    private TableColumn<Notification, Integer> idColumn;

    @FXML
    private TableColumn <Notification, String> dateColumn;

    @FXML
    private TableColumn <Notification, String> bookColumn;

    @FXML
    private TableColumn <Notification, String> messageColumn;

    @FXML
    public void deleteAction()
    {
        deleteNotificationFromServer(activeNotification);
        refreshNotificationList();
    }

    //for all tabs
    @FXML
    public void clickOnTable()
    {
        activeNotification = notificationsTable.getSelectionModel().getSelectedItem();
        setActiveNotificationDetails();
        System.out.println("active book id: " + notificationsTable.getSelectionModel().getSelectedItem().getIdNotification());
    }

    //NOT working because of the predicates at the end
    public void getNotificationsFromServer(){
        if (this.activeUser != null) {
            System.out.println(this.activeUser.getUsername());
        }
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "fetchNotifications";
        String payload = "";

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            Gson gson = new Gson();
            //Notification[] notifications = gson.fromJson(serverResponse,Notification[].class);
            Type collectionType = new TypeToken<Collection<Notification>>(){}.getType();
            Collection<Notification> collection = gson.fromJson(serverResponse, collectionType);
            notificationList.clear();
            notificationList.addAll(collection);
            List<Notification> list = (List<Notification>) collection;
            System.out.println(list);
//            System.out.println("filtered notifications: " + filterNotifications(list, isUser(activeUser.getUsername())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNotificationsForUser(String username){
        System.out.println(username);
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "fetchNotificationsForUser";
        String payload = new Gson().toJson(username);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            // Blocking this thread until the server responds
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);

            //deserialization from json: https://github.com/google/gson/blob/master/UserGuide.md#array-examples
            Gson gson = new Gson();
            //Notification[] notifications = gson.fromJson(serverResponse,Notification[].class);
            Type collectionType = new TypeToken<Collection<Notification>>(){}.getType();
            Collection<Notification> collection = gson.fromJson(serverResponse, collectionType);
            notificationList.clear();
            notificationList.addAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNotificationFromServer(Notification notification){
        //based on server_client_example
        ExecutorService es = Executors.newCachedThreadPool();

        String command = "removeNotification";
        String payload = new Gson().toJson(notification);

        System.out.println("Sending to server: \ncommand: " + command + ",\ndata: " + payload);
        SocketClientCallable commandWithSocket = new SocketClientCallable(HOSTNAME, PORT, command, payload);

        Future<String> response = es.submit(commandWithSocket);
        try {
            serverResponse = response.get();
            System.out.println("Response from server is : " + serverResponse);
            if (serverResponse.equals("Valid")) {
                System.out.println("Notification successfully deleted!");
            }
            else
                System.out.println("Deleting notification failed! Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //for all tabs
    private void initCols(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idNotification"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("book"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
    }

    public void setActiveNotificationDetails(){
        dateDetail.setText(activeNotification.getDate().toString());
        bookDetail.setText(activeNotification.getBook().toString());
        messageDetail.setText(activeNotification.getMessage());
    }

    public void refreshNotificationList(){
        getNotificationsForUser(activeUser.getUsername());
//        getNotificationsFromServer();
        initCols();
        notificationsTable.setItems(notificationList);
        if (!notificationList.isEmpty()) {
            activeNotification = notificationList.get(0);
            setActiveNotificationDetails();
        }
    }

    public void loadNotificationWithUser(User user){
        this.activeUser=user;
        refreshNotificationList();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
