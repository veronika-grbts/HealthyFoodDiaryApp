package HealthyDiaryApp.singleton;
import HealthyDiaryApp.entity.User;

/*
 * ApplicationContext class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє контекст програми, надаючи єдиний екземпляр і методи для керування поточним користувачем.
 * лише одного екземпляра класу ApplicationContext у всій програмі.
 */

public class ApplicationContext {

    private static volatile ApplicationContext instance;
    private User currentUser;

    private ApplicationContext() {
    }

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}

