package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    MessageService messageService = new MessageService();
    AccountService accountService = new AccountService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/", ctx -> ctx.result("Hello Javalin") );
        app.post("register", this::postRegisterHandler);
        app.post("login", this::postLoginHandler);
        app.post("messages", this::postMessagesHandler);
        app.get("messages", this::getMessagesHandler);

        return app;
    }

    /*
     * Handler to register a new user.
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        System.out.println("here postregister");
        ObjectMapper mapper = new ObjectMapper();
        Account newAccount = mapper.readValue(ctx.body(), Account.class);
        Account addedNewAccount = accountService.registerAccount(newAccount);

        if(addedNewAccount != null) {
            ctx.json(mapper.writeValueAsString(addedNewAccount));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /*
     * Handler for user login.
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginAccount = mapper.readValue(ctx.body(), Account.class);
        Account authenticatedAccount = accountService.loginAccount(loginAccount);

        if (authenticatedAccount != null) {
            // Login successful
            ctx.json(mapper.writeValueAsString(authenticatedAccount));
            ctx.status(200);
        } else {
            // Login failed
            ctx.status(401); // Unauthorized
        }
    }

    /*
     * Handler to submit a new message/post.
     */
    private void postMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);

        if(addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /*
     * Hnadler to get all the messages/posts.
     */
    private void getMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> allMessages = messageService.getAllMessages();

        ctx.json(mapper.writeValueAsString(allMessages));
        ctx.status(200);
    }

}