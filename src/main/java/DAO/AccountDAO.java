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

    /*
     * Add an account to the database.
     */
    public void addAccount(Account account) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account.getAccount_id());
            ps.setString(2, account.getUsername());
            ps.setString(3, account.getPassword());
            ps.executeUpdate();
            System.out.println("Account added successfully");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    void deleteAccount(Account account) {

    }
    void updateAccount(Account account) {

    }
}