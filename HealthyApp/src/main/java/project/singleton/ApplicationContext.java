package project.singleton;
import project.entity.User;

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

