package com.function.omep;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Azure Functions with HTTP Trigger.
 */

public class TimerTriggerJava1 {

    @FunctionName("TimerTriggerJava1")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        
        // Parse query parameter
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);

        // Get Environment Vars
        final String ACCOUNT_SID = System.getenv("ACCOUNT_SID");
        final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
        final String BOT_NUMBER = System.getenv("BOT_NUMBER");
        final String SEND_TO_NUMBER = System.getenv("SEND_TO_NUMBER");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Optional<String> bodyContent = request.getBody();

        // Check for request body data
        if (bodyContent.isPresent()) {
            context.getLogger().info("Body content: ");
            context.getLogger().info(bodyContent.get());

            String message_text = "Hello, OMEP! Here's the data: " + bodyContent.get();
            
            Message message = Message.creator(new PhoneNumber(SEND_TO_NUMBER), // sending to
            new PhoneNumber(BOT_NUMBER), // sending from
            message_text).create();
        } else {
            context.getLogger().info("Optional is NULL");

            Message message = Message.creator(new PhoneNumber(SEND_TO_NUMBER), // sending to
            new PhoneNumber(BOT_NUMBER), // sending from
            "Failed to get the data.").create();
        }
        
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);





        return request.createResponseBuilder(HttpStatus.OK).body("Yay").build();
        // if (name == null) {
        //     return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        // } else {
        //     return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        // }
    }
}
