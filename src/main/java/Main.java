import java.util.Scanner;

import Controller.SocialMediaController;
import DAO.AccountDAO;
import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {

    private static SocialMediaController controller = new SocialMediaController();
    private static AccountDAO accountDAO = new AccountDAO();

    public static void main(String[] args) {

        // SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);

    }

    public static SocialMediaController getController() {
        return controller;
    }

    public static AccountDAO geAccountDAO() {
        return accountDAO;
    }
}
