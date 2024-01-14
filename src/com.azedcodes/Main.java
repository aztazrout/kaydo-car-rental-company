import java.util.Scanner;

import user.UserAccountService;
import user.User;

public class Main {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        
        // welcome message
        System.out.println("\n\t\t\t\t\tWelcome to Kaydo!");
        System.out.println("\t\t\t\t  Your N°1 Car Rental Agency");

        // sign up / sign in
        UserAccountService.hasAccount();

        if(User.signedIn == true) {
            // display menu
        }





    }
    
}