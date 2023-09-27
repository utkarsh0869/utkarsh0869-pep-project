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
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::patchMessageHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountHandler);
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
     * Handler to get all the messages/posts.
     */
    private void getMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> allMessages = messageService.getAllMessages();

        ctx.json(mapper.writeValueAsString(allMessages));
        ctx.status(200);
    }

    /*
     * Handler to get messages/post by user id.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
    
        // Get the message_id from the URL path parameter.
        int messageId = ctx.pathParamAsClass("message_id", Integer.class).get();
        
        // Retrieve the message by its ID.
        Message message = messageService.getMessageById(messageId);

        ctx.status(200);
    
        if (message != null) {
            // Convert the message to JSON and send it in the response body.
            ctx.json(mapper.writeValueAsString(message));
        } else {
            // If no message is found, send an empty response body.
            ctx.result("");
        }
    }

    /*
     * Handler to delete message by message id.
     */
    private void deleteMessageHandler(Context ctx) {
        // Extract the message_id from the route parameters
        int messageId = ctx.pathParamAsClass("message_id", Integer.class).get();
    
        // Attempt to delete the message by its ID
        Message deletedMessage = messageService.deleteMessageById(messageId);
    
        if (deletedMessage != null) {
            // Message was deleted, return it in the response
            ctx.json(deletedMessage);
        } else {
            // Message did not exist, return an empty response
            ctx.status(200);
        }
    }
    
    /*
     * Handler to patch messages.
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message updatedMessage = mapper.readValue(ctx.body(), Message.class);
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        // Check if the message with the given message_id exists
        Message existingMessage = messageService.getMessageById(messageId);

        if (existingMessage != null) {
            // Validate the new message_text
            if (isValidMessageText(updatedMessage.getMessage_text())) {
                // Update the message in the database
                existingMessage.setMessage_text(updatedMessage.getMessage_text());
                messageService.updateMessage(existingMessage);

                // Return the updated message in the response body
                ctx.json(mapper.writeValueAsString(existingMessage));
            } else {
                // Invalid message_text, return 400 status
                ctx.status(400);
            }
        } else {
            // Message with the given message_id does not exist, return 400 status
            ctx.status(400);
        }
    }

    private boolean isValidMessageText(String messageText) {
        return messageText != null && !messageText.trim().isEmpty() && messageText.length() < 255;
    }

    /*
     * Handler to retrieve messages from a particular user id.
     */
    private void getMessagesByAccountHandler(Context ctx) throws JsonProcessingException {
        // Extract the account_id from the path parameter
        int accountId = ctx.pathParamAsClass("account_id", Integer.class).get();
    
        // Retrieve the list of messages posted by the specified user
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
    
        // Serialize the list of messages to JSON
        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(messages);
    
        // Set the response status and body
        ctx.status(200).json(responseJson);
    }
}