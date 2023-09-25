package DAO;

import java.util.List;

import Model.Account;

public interface AccountDAO {

    List<Account> getAccounts();

    void addAccount(Account account);
    void deleteAccount(Account account);
    void updateAccount(Account account);
}