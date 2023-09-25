package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public List<Account> getAccounts() {

        List<Account> accounts = new ArrayList<Account>();

        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt(1);
                String username = rs.getString(2);
                String password = rs.getString(3);

                Account account = new Account(id, username, password);
                accounts.add(account);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public void addAccount(Account account) {
        throw new UnsupportedOperationException("Unimplemented method 'addAccount'");
    }

    @Override
    public void deleteAccount(Account account) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccount'");
    }

    @Override
    public void updateAccount(Account account) {
        throw new UnsupportedOperationException("Unimplemented method 'updateAccount'");
    }
    
}
