package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;
import io.javalin.http.Context;

public class AccountService {
    
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void getAllAccounts(Context context) {
        System.out.println("here");
        List<Account> accounts = accountDAO.getAccounts();
        context.json(accounts);
        context.status(200);
    }

    // public List<Account> getAllAccounts() {
    //     return accountDAO.getAccounts();
    // }

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
}
