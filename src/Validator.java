//Brendan Loyd
//CS 3780
//November 7th, 2021
//Project 2

//This file handles validation of user inputs
import java.util.Scanner;

public class Validator {
    //Validates numeric inputs made by the user.
    public static int validateInputs(int min, int max, Scanner scanner) {
        boolean tryFlag = true;
        int input = -1;
        while(tryFlag) {
            try {
                input = scanner.nextInt();
                while (input < min || input > max) {
                    System.out.print("That is an invalid input. Please enter a number between " + min + " and " + max +": ");
                    input = scanner.nextInt();
                }
                tryFlag = false;
            } catch (Exception e) {
                System.err.print("Wrong input! Please enter an Integer: ");
                scanner.nextLine();
            }
        }
        return input;
    }

    //validates username
    public static String isValidUserName(Scanner scanner) {
        System.out.print("Username(Must be Characters only. At least 1 character and no longer than 12): ");
        String username = scanner.next();
        boolean Tryflag = true;
        while(Tryflag) {
            if (username.matches("[a-zA-Z]+") && username.length() > 1 && username.length() < 12) {
                Tryflag = false;
            } else {
                System.out.print("That is not a valid username format. Try again: ");
                scanner.nextLine();
                username = scanner.next();
            }
        }
        return username;
    }
    //I wrote this section for fun. Validates email.
    /*
    public static String isValidEmail(Scanner scanner) {

        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        System.out.print("Email: ");
        String email = scanner.next();

        boolean Tryflag = true;
        while(Tryflag) {
            if (email.matches(regex)) {
                Tryflag = false;
            } else {
                System.out.print("That is not a valid email format. Try again: ");
                scanner.nextLine();
                email = scanner.next();
            }
        }
        return email;
    }
     */

    //Validates password input
    public static String isValidPassword(Scanner scanner) {
        System.out.print("Password(Must be between 1 and 8 integers long. Integers only!: ");
        String password = scanner.next();
        boolean Tryflag = true;
        while(Tryflag) {
            if (password.matches("[0-9]+") && password.length() > 1 && password.length() < 9) {
                Tryflag = false;
            } else {
                System.out.print("That is not a valid password format. Try again: ");
                scanner.nextLine();
                password = scanner.next();
            }
        }
        return password;
    }
}
