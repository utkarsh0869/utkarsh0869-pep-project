package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if(!account.getUsername().isBlank() && !account.getUsername().trim().isEmpty()) {

            if(account.getPassword() != null && account.getPassword().length() >= 4) {
                
                if( !accountDAO.checkIfAccountAlreadyExists( account.getUsername() ) ) {
                    
                    int accountId = accountDAO.generateUniqueAccountId();

                    account.setAccount_id(accountId);

                    accountDAO.createAccount(account);

                    return account;
                }
            }
        }
        return null; // Registeration failed.
    }

    public Account loginAccount(Account loginAccount) {

        Account existingAccount = accountDAO.getAccountByUsername(loginAccount.getUsername());

        if(existingAccount != null) {
            if(loginAccount.getPassword().equals(existingAccount.getPassword())) {
                return existingAccount;
            }
        }
        // If no matching account is found or the passwords do not match, return null to indicate login failure.
        return null;
    }
}
