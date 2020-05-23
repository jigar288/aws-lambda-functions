package example.Handlers;

import java.util.ArrayList;
import java.util.Map;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import example.DynamoDB.DynamoDBClientFactory;



public class ReadDatabase implements RequestHandler<Map<String, String>, ArrayList<JSONObject> >  {

  private ArrayList<JSONObject>  accessDatabase(){

    String AWSprofile =  "default";
    String tableName = "uicCoursesFall2020";

    // * call the class to perform write to DB (class constructor needs region & profile, and tableName)
    DynamoDBClientFactory client = new DynamoDBClientFactory(Regions.US_EAST_1, AWSprofile, tableName);

    //* calling method to query database for all items
    ArrayList<JSONObject> dbEntries =  client.queryDatabase();
    return dbEntries;
  
  }

  @Override
  public ArrayList<JSONObject> handleRequest(Map<String, String> event, Context context) {

    // * call the dynamoDB Client to read all items from the database

    ArrayList<JSONObject> items = accessDatabase();
    
    return items;
  }

}