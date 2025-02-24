package Controller;

import java.util.ArrayList;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    // fields
    AccountService accountService;
    MessageService messageService;

    // Constructor
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postRegistrationHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountHandler);
        return app;
    }

    private void postRegistrationHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount)).status(200);
        } else{
            ctx.status(400);
        }
    }
    
    private void postLoginHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedinAccount = accountService.getLogin(account);
        if (loggedinAccount != null){
            ctx.json(mapper.writeValueAsString(loggedinAccount)).status(200);
        } else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int accountId = message.getPosted_by();
        if (!accountService.getAccountById(accountId)){
            ctx.status(400);
            return;
        }
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null){
            ctx.json(mapper.writeValueAsString(addedMessage)).status(200);
        } else{
            ctx.status(400);
        }
    }

    private void getAllMessageHandler(Context ctx){
        ArrayList<Message> allMessage = messageService.getAllMessage();
        ctx.json(allMessage).status(200);
    }

    private void getMessageByIdHandler(Context ctx){
        Message message = messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (message != null){
            ctx.json(message).status(200);
        } else{
            ctx.status(200);
        }
    }

    private void deleteMessageHandler(Context ctx){
        Message message = messageService.deleteMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (message != null){
            ctx.json(message).status(200);
        } else{
            ctx.status(200);
        }
    }

    private void patchMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message messageFromUser = mapper.readValue(ctx.body(), Message.class);
        Message message = messageService.updateMessageById(Integer.parseInt(ctx.pathParam("message_id")), messageFromUser);
        if (message != null){
            ctx.json(message).status(200);
        } else{
            ctx.status(400);
        }
    }

    private void getMessageByAccountHandler(Context ctx){
        ArrayList<Message> allMessage = messageService.getMessageByAccount(Integer.parseInt(ctx.pathParam("account_id")));
        ctx.json(allMessage).status(200);
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    /** 
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    */


}