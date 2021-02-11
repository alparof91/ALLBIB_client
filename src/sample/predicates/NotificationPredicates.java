package sample.predicates;

import sample.entity.Notification;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NotificationPredicates {

    public static Predicate<Notification> isUser(String user) {
        return p -> p.getUsername().equals(user);
    }

    public static List<Notification> filterNotifications (List<Notification> notificationList, Predicate<Notification> predicate){
        return notificationList.stream().filter(predicate).collect(Collectors.toList());
    }
}
