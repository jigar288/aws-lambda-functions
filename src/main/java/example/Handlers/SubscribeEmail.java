package example.Handlers;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import example.Notifications.Notifications;

import java.util.Map;

public class SubscribeEmail implements RequestHandler<Map<String, Object>, String> {
        
    @Override
    public String handleRequest(Map<String, Object> event, Context context) {

        final String email = (String) event.get("email"); //* getting req body

        //* subscribe via email
        //! fixme --> topicARN should a request parameter
        Notifications subscriber = new Notifications(Regions.US_EAST_1);
        final String topicARN = "arn:aws:sns:us-east-1:796567501476:CourseNotificationTopic";
        subscriber.subscribeUser(email, topicARN);

        return "Subscription Successful";
    }
    
} 