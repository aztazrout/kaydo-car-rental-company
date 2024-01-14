package user;

public class UserDAO {
    
    private static final int CAPACITY = 100;
    private static User[] users = new User[CAPACITY];

    private static int availableUsers = 0;
    

    public static User[] getAllUsers() {
        return users;
    }

    public static int getAvailableUsers() {
        return availableUsers;
    }

    public static void saveUser(User user) {
        // I should fix the problem if the capacity is over
        
        users[availableUsers] = user;
        availableUsers++;
    }

}
