package example.DynamoDB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import org.json.simple.JSONObject;

import example.Models.uicCoursesModel;



public class DynamoDBClientFactory {
    
    //* making private reference to dynamoDB & registering. This object allows you to interface w/ amazon dynamoDB w/ methods like (createTable, deleteTable, getItem. listItem, etc )
    // this is final since it should not change
    private final AmazonDynamoDBClientBuilder builder;
    private final String dbTableName;

    public DynamoDBClientFactory(Regions databaseRegion, String profile, String tableName){
        //! fixme - dont hardcode regions
        builder = AmazonDynamoDBClientBuilder.standard().withRegion(databaseRegion).withCredentials( DefaultAWSCredentialsProviderChain.getInstance() );
        dbTableName = tableName;
    }
    
    // * private: can't be accessed outside this class
    private AmazonDynamoDB createClient(){
        return builder.build();
    }

    // Publish a message to an Amazon SNS topic.
    //! move --> function to a separate file
    //! ARN & message should be parameters
    private void sendSNSNotifications(String subjectName, int CRN){
                
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials( DefaultAWSCredentialsProviderChain.getInstance() ).build();
        final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works: " + subjectName + " " + CRN;
        final String topicArn = "arn:aws:sns:us-east-1:796567501476:CourseNotificationTopic";
        final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        final PublishResult publishResponse = snsClient.publish(publishRequest);

        // Print the MessageId of the message.
        System.out.println("MessageId: " + publishResponse.getMessageId());   

    }

    public void makeDatabaseEntry(String subjectName, int CRN, DynamoDBClientFactory client, Map<String, Object> extraClassInfo){
        
       DynamoDB databaseInstance = new DynamoDB(client.createClient() );   
       
       //needed to add "package example" at the top of file "uicCoursesModel.java" to use this class
       uicCoursesModel uicClassObj = new uicCoursesModel(subjectName, CRN, extraClassInfo);

       try{

           //* getting a reference to the table
           Table table = databaseInstance.getTable(dbTableName);
           //todo: make additional info for table

           //* making an entry to the table
           PutItemOutcome outcomeObj = table.putItem(new Item().withPrimaryKey("courseName", uicClassObj.getCourseName(), "CRN", uicClassObj.getCRN()).withMap("class-info", extraClassInfo));
           System.out.println("Database entry outcome: " + outcomeObj.getPutItemResult());

       }catch (Exception e){
           System.err.println("Error writing to the table ");
           System.err.println(e.getMessage());
       }

       //* sending out notifications for database entry
       sendSNSNotifications(subjectName, CRN);

    }

    public ArrayList<JSONObject> queryDatabase(){

        //* get reference to the databae object
        DynamoDB databaseInstance = new DynamoDB( this.createClient() );    

        //* get reference to the table
        Table table = databaseInstance.getTable(dbTableName);

        ScanSpec scanConditions = new ScanSpec().withProjectionExpression("courseName, CRN");

        ArrayList<JSONObject> databaseJSONObj = new ArrayList<JSONObject>();
        
                        
        try {

            //* call scan method from table object to get items
            ItemCollection<ScanOutcome> items = table.scan(scanConditions);

            //* make an iterator object
            Iterator<Item> iterObj = items.iterator();
        
            //* iterate over items via loop
            while(iterObj.hasNext()){ //* as long as there is a next item --> keep looping

                // * using Gson to convert to JSON
                Item oneItem = iterObj.next(); //* this gets the current item
            
                Map<String, Object> elements = oneItem.asMap();
                JSONObject jsonItem = new JSONObject(elements);
                databaseJSONObj.add(jsonItem);

            }
                                
        } catch (Exception e) {
            System.err.println("Error reading to the table ");
            System.err.println(e.getMessage());
        }

        return databaseJSONObj;

    }

    public JSONObject searchClass(int CRN){

        //* get reference to the databae object
        DynamoDB databaseInstance = new DynamoDB( this.createClient() );    

        //* get reference to the table
        Table table = databaseInstance.getTable(dbTableName);

        //! this should be provided from post req body 
        //* setting search conditions 
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("courseName", "Computer Science", "CRN", CRN);
        JSONObject jsonObj = new JSONObject();
        
        try {    
            //* querying db for item
            System.out.println("Attempting to search for item ");
            Item outcome = table.getItem(spec);
            System.out.println("Search result: " + outcome);

            //* lets see what you remember --> how do I convert outcome to JSON object? 

            //convert it to a java map
            Map<String, Object> jsonMap = outcome.asMap();

            //* then pass in the json Map to JSONObject constructor
            jsonObj = new JSONObject(jsonMap);

        } catch (Exception e) {
            System.err.println("Error searching for item in the table ");
            System.err.println(e.getMessage());
        }

        return jsonObj;

    }

}