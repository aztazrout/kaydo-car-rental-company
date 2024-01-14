package user;

import java.util.Scanner;

public class UserAccountService {

    private static Scanner scan = new Scanner(System.in);

    /**
     * This method serves as a sign in / sign up page. It will redirect the user to
     * create a new account or to login into his existent account. If it's the admin, he can also sign in
     * using his secret credentials
     */
    public static void hasAccount() {

        String input;

        do {
            System.out.println("\nPlease enter \"YES\" to confirm that you already have an account at Kaydo.");
            System.out.print("Otherwise, enter \"NO\" to create your new account. \n> ");

            input = scan.nextLine().toLowerCase().trim();

        } while (!input.equals("yes") && !input.equals("no") && !input.equals("admin"));

        if (input.equals("admin")) {
            // sign in as admin
        } else if (input.equals("no")) {
            createAccount();
            signIn();
        } else {
            signIn();
        }
    }

    /**
     * This method asks the user for all the necessary informations to create his
     * account
     */
    public static void createAccount() {

        System.out.println("\nYou are a few steps away from joining Kaydo's family ... Let's create your account!\n");

        System.out.print("Enter your first name : \n> ");
        String firstName = scan.nextLine();

        System.out.print("Enter your last name : \n> ");
        String lastName = scan.nextLine();

        String userName = readUsername();

        String password = readPassword();

        String email = readEmail();

        User user = new User(userName, password);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setID();

        UserDAO.saveUser(user);

        System.out.println("\nYohooo! Account created succesfully.\n");
    }

    public static User signIn() {

        System.out.println("We're excited to sign you in! \n");

        System.out.print("Enter username: \n> ");
        String username = scan.nextLine().trim();
        username = matchUsername(username);

        System.out.print("Enter password : \n> ");
        String password = scan.nextLine().trim();

        User currentUser = findUser(username);
        
        System.out.println(matchPassword(username, password));

        if(matchPassword(username, password)) {
             User.signedIn = true;
             User.currentUser = currentUser;
             System.out.println("\n" + "We're happy to see you in " + currentUser.getFirstName() + "!\n");
        }

        return currentUser;

    }

    private static String readUsername() {

        String userName = "";
        boolean firstTrial = true;
        boolean isValid;

        do {
            if (firstTrial) {
                System.out.print("\nEnter your username : \n> ");
            }

            if (!firstTrial) {
                if (userName.length() <= 5) {
                    System.out.println("Your username should be more than 5 characters.");
                } else if (doesUsernameExist(userName)) {
                    System.out.print("This username is already taken.");
                }

                System.out.print("Please enter a valid username : \n> ");
            }

            userName = scan.nextLine();
            firstTrial = false;
            isValid = (userName.length() > 5) && (!doesUsernameExist(userName));

        } while (!isValid);

        return userName;
    }

    private static boolean doesUsernameExist(String usernameInput) {

        User[] users = UserDAO.getAllUsers();
        int availableUsers = UserDAO.getAvailableUsers();

        for (int i = 0; i < availableUsers; i++) {
            User user = users[i];

            if (user == null) {
                continue;
            }

            if (user.getUserName().equals(usernameInput)) {
                return true;
            }
        }

        return false;
    }

    private static String readPassword() {

        boolean isValid;
        boolean firstTrial = true;
        String password = "";

        do {

            if (firstTrial) {
                System.out.print("Enter your password : \n> ");
            }

            if (!firstTrial) {

                System.out.println("Your password is invalid. Please try again:");
                System.out.println("\t- Your password should be at least 10 characters or more.");
                System.out.println("\t- A mixture of both uppercase and lowercase letters.");
                System.out.println("\t- A mixture of letters and numbers.");
                System.out.print("\t- Inclusion of at least one special character. For example : ! @ # ? ] \n> ");
            }

            password = scan.nextLine();
            firstTrial = false;
            isValid = isPasswordValid(password);

        } while (!isValid);

        return password;
    }

    private static boolean isPasswordValid(String password) {

        boolean correctSize = password.length() >= 10;
        boolean containsLetter = false;
        boolean containsNumber = false;
        boolean containsUpperCase = false;
        boolean containsLowerCase = false;
        boolean containsSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {

            char current = password.charAt(i);
            int currentASCII = (int) current;

            if ((currentASCII >= 32 && currentASCII <= 47) || (currentASCII >= 58 && currentASCII <= 64)) {
                containsSpecialCharacter = true;
            } else if (currentASCII >= 48 && currentASCII <= 57) {
                containsNumber = true;
            } else if (currentASCII >= 65 && currentASCII <= 90) {
                containsLetter = true;
                containsUpperCase = true;
            } else if (currentASCII >= 97 && currentASCII <= 122) {
                containsLetter = true;
                containsLowerCase = true;
            }

        }

        boolean isPasswordValid = correctSize && containsLetter && containsNumber && containsLowerCase
                && containsUpperCase && containsSpecialCharacter;

        return isPasswordValid;
    }

    private static String readEmail() {

        String email = "";
        boolean isValid;
        boolean firstTrial = true;

        do {
            if (firstTrial) {
                System.out.print("\nEnter your email : \n> ");
            }

            if (!firstTrial) {
                System.out.print("Please enter a valid email : \n>");
            }

            email = scan.nextLine();
            firstTrial = false;
            isValid = email.contains("@") && email.contains(".") && !doesEmailExist(email);

        } while (!isValid);

        return email;

    }

    private static boolean doesEmailExist(String email) {

        User[] users = UserDAO.getAllUsers();
        int availableUsers = UserDAO.getAvailableUsers();

        for (int i = 0; i < availableUsers; i++) {
            User user = users[i];

            if (user == null) {
                continue;
            }

            if (user.getEmail().equals(email)) {
                return true;
            }

        }
        return false;
    }

    private static String matchUsername(String username) {

        final int MAX_TRIALS = 3;
        int trials = 1;

        boolean isValid = doesUsernameExist(username);

        while (!isValid && trials <= MAX_TRIALS) {

            if (trials == MAX_TRIALS) {
                System.out.println("This username doesn't exist. You are not allowed to enter your username anymore.");
                System.out.println("Please come back later on! ");
                System.exit(0);
            }

            
            System.out.print("This username doesn't exist. Please enter a valid username: \n> ");
            username = scan.nextLine().trim();
            isValid = doesUsernameExist(username);
            
            trials++;
        }

        return username;

    }

    private static boolean matchPassword(String username, String passwordInput) {

        User currentUser = findUser(username);
        String currentPassword = currentUser.getPassword();
        boolean isValid = currentPassword.equals(passwordInput);

        final int MAX_TRIALS = 3;
        int trials = 1;

        while (!isValid && trials <= MAX_TRIALS) {

            if (trials == MAX_TRIALS) {
                System.out.println("Wrong Password. You are not allowed to enter your password anymore.");
                System.out.println("Please come back later on! ");
                System.exit(0);
            }

            System.out.print("Wrong password. Please enter the correct password: \n> ");
            passwordInput = scan.nextLine().trim();
            isValid = currentPassword.equals(passwordInput);

            trials++;
        }

        return isValid;
    }

    private static User findUser(String username) {

        User[] users = UserDAO.getAllUsers();
        int availableUsers = UserDAO.getAvailableUsers();
        

        for(int index = 0; index < availableUsers; index++ ) {
            User currentUser = users[index];

            if(currentUser == null) {
                continue;
            }

            if(currentUser.getUserName().equals(username)) {
                return  currentUser;
            }

        }

        return null;
    }

}
