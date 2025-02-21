package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account){
        String username = account.getUsername();
        String password = account.getPassword();
        if (username == null || username.isEmpty()){
            return null;
        }
        if (password == null || password.length() < 4){
            return null;
        }
        return accountDAO.insertAccount(account);
    }
}