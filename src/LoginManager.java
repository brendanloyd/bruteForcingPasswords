//Brendan Loyd
//CS 3780
//November 7th, 2021
//Project 2

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.*;
import java.security.*;

public class LoginManager {

    //writes to plainTextPair.txt
    public static void writePlainTextPairFile(String username, String password) throws IOException {
        try(FileWriter f = new FileWriter("plainTextPair.txt", true);
        BufferedWriter b = new BufferedWriter(f);
        PrintWriter p = new PrintWriter(b)) {

            p.println(username);
            p.println(password);
        }
    }
    //generates hash
    public static String generateHash(MessageDigest digest, String password) {
        byte[] bytes = digest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String generateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return generateHash(digest, password);
    }

    public static String generateHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        return generateHash(digest, password);
    }
    //writes to hashedPassFile.txt
    public static void writeHashedPassFile(String username, String password) throws IOException {
        try(FileWriter f = new FileWriter("hashedPassFile.txt", true);
            BufferedWriter b = new BufferedWriter(f);
            PrintWriter p = new PrintWriter(b)) {
            p.println(username);
            p.println(generateHash(password));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //returns salt for salted hash.
    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[1];
        random.nextBytes(salt);
        return salt;
    }

    //writes to hashedPassWithSalt.txt
    public static void writeHashedPassWithSaltFile(String username, String password) throws IOException, NoSuchAlgorithmException {
        String encodedHash;
        try( FileWriter f = new FileWriter("hashedPassWithSaltFile.txt", true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b)) {
            byte[] salt = getSalt();
            encodedHash = generateHash(password, salt);
            p.println(username);
            p.println(salt[0]);
            p.println(encodedHash);
        }
    }
    //file writer method to handle writing to files.
    public static void fileWriter(String username, String password) throws IOException, NoSuchAlgorithmException {
        writePlainTextPairFile(username, password);
        writeHashedPassFile(username, password);
        writeHashedPassWithSaltFile(username, password);
    }
    //this method collects information from the user to create an account.
    public static void createNewAccount(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        String username = Validator.isValidUserName(scanner);
        //String email = isValidEmail(scanner);
        String password = Validator.isValidPassword(scanner);
        fileWriter(username, password);
    }

    //This method returns a random password for generated accounts
    public static String getAlphaString(int n) {

        // chose a Character random from this String
        String AlphaString = "0123456789";

        // create StringBuffer size of AlphaString
        int passSize = 1 + (int)(Math.random() * n);
        StringBuilder sb = new StringBuilder(passSize);

        for (int i = 0; i < passSize; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaString.charAt(index));
        }
        return sb.toString();
    }

    //This method returns a random username for generated accounts
    public static String getNumericString(int n) {

        // chose a Character random from this String
        String NumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(NumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(NumericString.charAt(index));
        }
        return sb.toString();
    }

    //Handles creating the randomly generated accounts
    public static void createRandomAccounts(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        System.out.print("How many accounts would you like to create between 1-200: ");
        int numOfAccounts = Validator.validateInputs(1, 200, scanner);
        System.out.print("Length of passwords value (total range possible is from 1 to 8):");
        int passLength = Validator.validateInputs(1, 8, scanner);
        int i;
        for(i = 0; i < numOfAccounts; i++) {
            fileWriter(getNumericString(12), getAlphaString(passLength));
            }
        }

    //reads plainTextPair.txt
    public static boolean readPlainTextPairFile(String username, String password) throws IOException {
        File file = new File("plainTextPair.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            if (st.equals(username)) {
                if( br.readLine().equals(password)) {
                    System.out.println("Username and password match. Access granted.");
                    return true;
                } else {
                    System.out.println("Incorrect Password.");
                }
            }
        }
        return false;
    }

    //reads hashedPassFile.txt
    public static boolean readHashedPassFile(String username, String password) throws IOException, NoSuchAlgorithmException {
        File file = new File("hashedPassFile.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        String st, encodedHash;

        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        encodedHash = sb.toString();

        while ((st = br.readLine()) != null) {
            if (st.equals(username)) {
                if( br.readLine().equals(encodedHash)) {
                    return true;
                }
            }
        }
        return false;
    }

    //reads hashedPassWithSaltFile.txt
    public static boolean readHashedPassWithSaltFile(String username,String password) throws IOException, NoSuchAlgorithmException {
        File file = new File("hashedPassWithSaltFile.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String st, encodedHash;

        while ((st = br.readLine()) != null) {
            if (st.equals(username)) {
                String saltFromFile = br.readLine();

                digest.update(Byte.parseByte(saltFromFile));
                byte[] bytes = digest.digest(password.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte aByte : bytes) {
                    sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                }
                encodedHash = sb.toString();
                if( br.readLine().equals(encodedHash)) {
                    return true;
                }
            }
        }
        return false;
    }

    //method handling reading all three files
    public static boolean fileReader(String username, String password) throws IOException, NoSuchAlgorithmException {
        boolean flag = readPlainTextPairFile(username, password);
        flag = readHashedPassFile(username, password);
       flag = readHashedPassWithSaltFile(username, password);
       return flag;
    }

    //Authenticates existing accounts by calling fileReader.
    public static void authenticateExistingAccount (Scanner scanner) throws IOException, NoSuchAlgorithmException {
        String username = Validator.isValidUserName(scanner);
        //String email = isValidEmail(scanner);
        String password = Validator.isValidPassword(scanner);
        boolean flag = fileReader(username, password);
        if(flag) {
            System.out.println("All files passed. Access granted.");
        } else {
            System.out.println("Incorrect password.");
        }
    }

    //This method attempts to bruteforce type2 files.
    public static Cracked passwordCracker(HashMap<String, ArrayList<String>> userData, Scanner scanner) throws NoSuchAlgorithmException {
        ArrayList<String> passwords = new ArrayList<>();
//        for(int length = 0; length < 8; length++) {
//            for(int digit = 0; digit < 10; digit++) {
//                if(length == 0) {
//                    passwords.add("" + digit);
//                } else {
//                    for(int cat = (length - 1) * 10; cat < ((length - 1) * 10) + 10; cat++) {
//                        String total = passwords.get(cat) + "" + digit;
//                        passwords.add(total);
//                    }
//                }
//            }
//        }
        System.out.println("Pick a minimum length of password to crack(Between 1 and 8): ");
        int choice = Validator.validateInputs(1, 8, scanner);



        for (int number = 0; number < 100000000; number++) {
            String hash = generateHash(number + "");

            if (userData.get(hash) != null && (number + "").length() >= choice) {
                return new Cracked(number + "", hash, userData.get(hash));
            }
        }

        return null;
    }

    //Specifics to bruteForcing type2 files
    public static void bruteForceType2(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        Instant before = Instant.now();
        File file = new File("hashedPassFile.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String username, encodedHash;
        HashMap<String, ArrayList<String>> userData = new HashMap<>();

        while ((username = br.readLine()) != null) {
            encodedHash = br.readLine();
            userData.putIfAbsent(encodedHash, new ArrayList<>());
            userData.get(encodedHash).add(username);
        }

        Cracked info = passwordCracker(userData, scanner);
        Instant after = Instant.now();
        long delta = Duration.between(before, after).toMillis();
        System.out.println("Usernames cracked: "+ info.getUsernames() + "\nPassword is: " + info.getPassword() +
                "\nThe time in ms it took: " + delta);

    }
    //Specifics to bruteForcing type3 files
    public static Cracked passwordCracker3(HashMap<String, HashMap<String, ArrayList<String>>> userData, Scanner scanner) throws NoSuchAlgorithmException {
        System.out.println("Pick a minimum length of password to crack(Between 1 and 8): ");
        int choice = Validator.validateInputs(1, 8, scanner);

        ArrayList<String> salts = new ArrayList<>(userData.keySet());
        for (int number = 0; number < 100000000; number++) {
            for (int i = 0; i < salts.size(); i++) {
                String salt = salts.get(i);
                byte[] saltySeaDog = {Integer.valueOf(salt).byteValue()};
                String hash = generateHash(number + "", saltySeaDog);

                if (userData.get(salt).get(hash) != null && (number + "").length() >= choice) {
                    return new Cracked(number + "", hash, salts.get(i), userData.get(salt).get(hash));
                }
            }
        }

        return null;
    }

    //Specifics to bruteForcing type3 files
    public static void bruteForceType3(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        Instant before = Instant.now();
        File file = new File("hashedPassWithSaltFile.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String username, salt, encodedHash;
        HashMap<String, HashMap<String, ArrayList<String>>> userData = new HashMap<>();
        while ((username = br.readLine()) != null) {
            salt = br.readLine();
            encodedHash = br.readLine();
            userData.putIfAbsent(salt, new HashMap<>());
            userData.get(salt).putIfAbsent(encodedHash, new ArrayList<>());
            userData.get(salt).get(encodedHash).add(username);
        }
        Cracked info = passwordCracker3(userData, scanner);
        Instant after = Instant.now();
        long delta = Duration.between(before, after).toMillis();
        System.out.println("Usernames cracked: "+ info.getUsernames() + "\nPassword is: " + info.getPassword() +
                "\nThe time in ms it took: " + delta);
    }

    //handles calling all necessary methods to bruteForce
    public static void bruteForceAttack(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        System.out.println("You can attempt to bruteforce:\n" +
                "(1) File of type 2 (having a hashed password).\n" +
                "(2) File of type 3 (having a salted hash password):");
        int choice = Validator.validateInputs(1, 2, scanner);
        switch (choice) {
            case 1:
                bruteForceType2(scanner);
                break;
            case 2:
                bruteForceType3(scanner);
                break;
        }
    }
    //shows a menu to the user and recives input
    public static void menu() throws IOException, NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to: \n" +
                "(1) Create an account. \n" +
                "(2) Create random accounts. \n" +
                "(3) Authenticate an existing account. \n" +
                "(4) Attempt to bruteforce a password file.");
        int choice = Validator.validateInputs(1, 4, scanner);
        switch (choice) {
            case 1:
                createNewAccount(scanner);
                break;
            case 2:
                createRandomAccounts(scanner);
                break;
            case 3:
                authenticateExistingAccount(scanner);
                break;
            case 4:
                bruteForceAttack(scanner);
                break;
        }
    }
    //calls menu to start the program.
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        menu();
    }
}
