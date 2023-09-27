package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database.
 */
public class AccountDAO {

    /*
     * Retrieve all the accounts from the account table. 
     */
    public List<Account> getAccounts() {

        List<Account> accounts = new ArrayList<Account>();

        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                Account account = new Account(id, username, password);
                accounts.add(account);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public boolean checkIfAccountAlreadyExists(String username) {

        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT COUNT(*) FROM account where username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                int count  = rs.getInt(1);
                return count > 0; // If count is greater than 0, the account already exists.
            }

            return false; // Account does not exists. 
        } catch (SQLException e) {
            throw new RuntimeException("Error checking account existence: " + e.getMessage());
        }
    }

    public int generateUniqueAccountId() {

        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT MAX(account_id) FROM account";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                int maxAccountId = rs.getInt(1);
                // Increment the highest account_id to generate a new unique account_id
                return maxAccountId + 1;
            }

            // If there are no existing accounts, start with account_id 1
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating unique account ID: " + e.getMessage());
        }
    }
    /*
     * Add an account to the database.
     */
    public void createAccount(Account account) {

        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Here");

            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating account: " + e.getMessage());
        }
    }

}