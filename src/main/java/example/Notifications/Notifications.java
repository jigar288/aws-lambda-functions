package example.Notifications;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;

import java.util.List;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;

public class Notifications {

    final private AmazonSNS snsClient;

    public Notifications(Regions NotificationsRegion){
        //* creating reference to SNS client
        snsClient = AmazonSNSClientBuilder.standard().withRegion(NotificationsRegion).withCredentials( DefaultAWSCredentialsProviderChain.getInstance() ).build();
    }
    
    // * that way it forces you to supply region for subscription --> so that you can sub/unsub from the right region 
    public void subscribeUser(String email, String topicARN){
        
        // * subscribe an email endpoint to SNS
        final SubscribeRequest subscribeRequest = new SubscribeRequest(topicARN, "email", email );

        //* send user subscription 
        this.snsClient.subscribe(subscribeRequest);

        System.out.println("SubscribeRequest: " + snsClient.getCachedResponseMetadata(subscribeRequest));
    }

    // * this function will return the sub-ARN as string. If not found it will stay null
    private String findSubscriptionARN(String email, String topicARN, AmazonSNS snsClient){
        String subscriptionARN = null; 
        ListSubscriptionsByTopicResult subList = snsClient.listSubscriptionsByTopic(topicARN);
        List<Subscription> listOfSubscriptions = subList.getSubscriptions();


        //* loop over to find Subscription ARN
        for (Subscription subscription : listOfSubscriptions) {
            if(email.equals(subscription.getEndpoint())){
                subscriptionARN = subscription.getSubscriptionArn();
                break;
            }
        }

        return subscriptionARN;

    }

    public void unsubscribeUser(String email, String topicARN){

        final String subscriptionARN = findSubscriptionARN(email, topicARN, this.snsClient); //* looking for user's info to perform unsubscription

        // * this section finishes off the unsubscription proceess
        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
        unsubscribeRequest.setSubscriptionArn(subscriptionARN);
        this.snsClient.unsubscribe(unsubscribeRequest);    




    }

    // Publish a message to an Amazon SNS topic.
    //! fixme - ARN & message should be req parameters
    public void sendSNSNotifications(String subjectName, int CRN){
                
        final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works: " + subjectName + " " + CRN;
        final String topicArn = "arn:aws:sns:us-east-1:796567501476:CourseNotificationTopic";
        final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        final PublishResult publishResponse = snsClient.publish(publishRequest);

        // Print the MessageId of the message.
        System.out.println("MessageId: " + publishResponse.getMessageId());   

    }    
    

}