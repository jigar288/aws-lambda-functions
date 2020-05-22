package example.Notifications;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;

import java.util.List;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;

public class Notifications {
    
    // ! email should be passed in from lambda event object
    // ! check validity of email & ARN before processing --> check if SDK handles it
    public void subscribeUser(String email, String topicARN){
        //* reference to SNS client
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials( DefaultAWSCredentialsProviderChain.getInstance() ).build();

        // * subscribe an email endpoint to SNS
        final SubscribeRequest subscribeRequest = new SubscribeRequest(topicARN, "email", email );

        //* send user subscription 
        snsClient.subscribe(subscribeRequest);

        System.out.println("SubscribeRequest: " + snsClient.getCachedResponseMetadata(subscribeRequest));

        //! parse the subscription result --> return as lambda function response
    }

    // * this function will return the sub-ARN as string. If not found it will stay null
    //! code for null arn
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


    //! pass in email from 
    //! make a separate lambda function for this method

    //?  if possible --> 
        //! return detailed error message on failure & using exceptions


    public String unsubscribeUser(String email, String topicARN){

        //* reference to SNS client & info for subscription
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials( DefaultAWSCredentialsProviderChain.getInstance() ).build();

        final String subscriptionARN = findSubscriptionARN(email, topicARN, snsClient); //* looking for user's info to perform unsubscription

        //? can I use == here?
        if(subscriptionARN != null){
            // * this section finishes off the unsubscription proceess
            UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
            unsubscribeRequest.setSubscriptionArn(subscriptionARN);
            snsClient.unsubscribe(unsubscribeRequest);    
            return "Unsubscription was successful";        
        }

        return "Unsubscription failed"; 


        //* notes on few things I'm learning

        // * unsubscribe an email endpoint to SNS
        //? why is it using final keyword here? --> this should not change --> are there other places where I should be using it
        //* setting

        //? how can I test for an expected output w/o fully deploying my code in the cloud? --> write some test cases which will run as you deploy  --> I bet there is a way to just run the test cases w/o deploying it

        
    
        //* send user subscription 
    }
    

}