package example.Handlers;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import example.Notifications.Notifications;

import java.util.Map;

public class SubscribeEmail implements RequestHandler<Map<String, Object>, String> {

        
    @Override
    public String handleRequest(Map<String, Object> event, Context context) {

        // todo: get email from event object

        //* subscribe via email
        Notifications subscriber = new Notifications();
        String topicARN = "arn:aws:sns:us-east-1:796567501476:CourseNotificationTopic";
        subscriber.subscribeUser("jigar@novusclub.org", topicARN);

        return "Is this the correct response???";
    }
    
}