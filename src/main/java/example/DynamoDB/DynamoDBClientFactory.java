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
import example.Notifications.Notifications;



public class DynamoDBClientFactory {
    
    //* making private reference to dynamoDB & registering. This object allows you to interface w/ amazon dynamoDB w/ methods like (createTable, deleteTable, getItem. listItem, etc )
    // this is final since it should not change
    private final AmazonDynamoDBClientBuilder builder;
    private final DynamoDB databaseInstance;
    private final Table table;

    public DynamoDBClientFactory(Regions databaseRegion, String profile, String tableName){
        //! fixme - dont hardcode regions
        builder = AmazonDynamoDBClientBuilder.standard().withRegion(databaseRegion).withCredentials( DefaultAWSCredentialsProviderChain.getInstance() );
        databaseInstance = new DynamoDB( builder.build() ); 
        table = databaseInstance.getTable(tableName); 
    }

    public void makeDatabaseEntry(String subjectName, int CRN, DynamoDBClientFactory client, Map<String, Object> extraClassInfo){
        
       uicCoursesModel uicClassObj = new uicCoursesModel(subjectName, CRN, extraClassInfo);

       try{

           //* getting a reference to the table -->  making an entry to the table
           PutItemOutcome outcomeObj = this.table.putItem(new Item().withPrimaryKey("courseName", uicClassObj.getCourseName(), "CRN", uicClassObj.getCRN()).withMap("class-info", extraClassInfo));
           System.out.println("Database entry outcome: " + outcomeObj.getPutItemResult());

       }catch (Exception e){
           System.err.println("Error writing to the table ");
           System.err.println(e.getMessage());
       }


       //* sendSNSNotifications is a static method ClassName.methodName() is the way to invoke a static method
       //* sending out notifications for database entry
       Notifications.sendSNSNotifications(subjectName, CRN);
       
    }

    public ArrayList<JSONObject> queryDatabase(){

        ScanSpec scanConditions = new ScanSpec().withProjectionExpression("courseName, CRN");
        ArrayList<JSONObject> databaseJSONObj = new ArrayList<JSONObject>();
        
        try {

            //* call scan method from table object to get items
            ItemCollection<ScanOutcome> items = this.table.scan(scanConditions);

            //* make an iterator object
            Iterator<Item> iterObj = items.iterator();
        
            while(iterObj.hasNext()){ 

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

        //* setting search conditions 
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("courseName", "Computer Science", "CRN", CRN);
        JSONObject jsonObj = new JSONObject();
        
        try {    
            //* querying db for item
            System.out.println("Attempting to search for item ");
            Item outcome = this.table.getItem(spec);
            System.out.println("Search result: " + outcome);

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