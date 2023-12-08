package JDBC_Project.BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static String url = "jdbc:mysql://localhost:3306/bank_db";
    private static String username = "root";
    private static String password = "root123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con, sc);
            Accounts accounts = new Accounts(con, sc);
            AccountManager accountManager = new AccountManager(con, sc);

            String email;
            long account_number;

            while (true) {
                System.out.println("***- Welcome to Banking System -***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                int choice1 = sc.nextInt();
                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");
                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("1. Open a New Bank Account");
                                System.out.println("2. Exit");
                                System.out.print("Enter your choice: ");

                                if (sc.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your Choice: ");
                                choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;

                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;

                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;

                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;

                                    case 5:
                                        break;

                                    default:
                                        System.out.println("Please, Enter valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password");
                            System.out.println();
                            break;
                        }

                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM");
                        System.out.println("EXISTING SYSTEM");
                        return;

                    default:
                        System.out.println("Please, Enter Valid Choice!");
                        System.out.println();
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
