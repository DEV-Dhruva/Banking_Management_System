package JDBC_Project.BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection con;
    private Scanner sc;

    public AccountManager(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void debit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement ps = con
                        .prepareStatement("select * from accounts where account_number = ? and security_pin = ?;");
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double current_balance = rs.getLong("balance");
                    if (amount <= current_balance) {
                        String debit_query = "update accounts set balance = balance - ? where account_number = ?;";

                        PreparedStatement ps1 = con.prepareStatement(debit_query);
                        ps1.setDouble(1, amount);
                        ps1.setLong(2, account_number);
                        int affectedRows = ps1.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Rs." + amount + " debited Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance");
                    }
                } else {
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void credit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement ps = con
                        .prepareStatement("select * from accounts where account_number = ? and security_pin = ?;");
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String credit_query = "update accounts set balance = balance + ? where account_number = ?;";

                    PreparedStatement ps1 = con.prepareStatement(credit_query);
                    ps1.setDouble(1, amount);
                    ps1.setLong(2, account_number);
                    int affectedRows = ps1.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Rs." + amount + "credited Suucessfully");
                        con.commit();
                        con.setAutoCommit(true);
                    } else {
                        System.out.println("Transaction Failed");
                        con.rollback();
                        con.setAutoCommit(true);
                    }

                } else {
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                PreparedStatement ps = con
                        .prepareStatement("select * from accounts where account_number = ? and security_pin = ?;");
                ps.setLong(1, sender_account_number);
                ps.setString(2, security_pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double current_balance = rs.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "update accounts set balance = balance - ? where account_number = ?;";
                        String credit_query = "update accounts set balance = balance + ? where account_number = ?;";

                        PreparedStatement debitps = con.prepareStatement(debit_query);
                        PreparedStatement creditps = con.prepareStatement(credit_query);

                        debitps.setDouble(1, amount);
                        debitps.setLong(2, sender_account_number);
                        creditps.setDouble(1, amount);
                        creditps.setLong(2, receiver_account_number);

                        int affectedRows1 = debitps.executeUpdate();
                        int affectedRows2 = creditps.executeUpdate();

                        if (affectedRows1 > 0 && affectedRows2 > 0) {
                            System.out.println("Transaction Successfull");
                            System.out.println("Rs." + amount + " Transferred Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction Failed due to Invalid Account Number or Security Pin");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    System.out.println("Invalid Account Number");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void getBalance(long account_number) {
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            PreparedStatement ps = con
                    .prepareStatement("select balance from accounts where account_number = ? and security_pin = ?;");
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Balance: Rs." + balance);
            } else {
                System.out.println("Invalid Security Pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
