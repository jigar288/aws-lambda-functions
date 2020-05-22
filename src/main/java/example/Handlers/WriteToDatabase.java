package example.Handlers;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import example.DynamoDB.DynamoDBClientFactory;

import java.util.HashMap;
import java.util.Map;

// ! not sure what to do next? --> look for ! or todo: comments

// Handler value: example.Handler
public class WriteToDatabase implements RequestHandler<Map<String, Object>, String> {
  
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  // * validates input to ensure database entry it correct --> returns boolean if
  //! valid input --> don't forget to do this
  public Boolean isValidInput(Map<String, String> event) {

    // * check if input is empty or not and valid data --> this would be useful for
    // APIs --> but its good practice to write code that validates input
    // * let first assume data is correct --> focus on core functionality

    // * if I had a selection menu for users then I wouldn't need to validate the
    // data --> there is no chance for human error
    // * however, don't school depts have to manually enter class data

    // * once that data is available you can pass it to users
    // * whether thats dept admins --> approving classes to be taught
    // * or students picking classes

    return true;
  }

  // * this function takes the input from the lambda function & stores it into  
  public void writeToDatabase(Map<String, Object> event) {
        
    String AWSprofile =  "default";
    String tableName = "uicCoursesFall2020";

    // * call the class to perform write to DB (class constructor needs region & profile, and tableName)
    DynamoDBClientFactory client = new DynamoDBClientFactory(Regions.US_EAST_1, AWSprofile, tableName);

    //* type cast input 
    String subject = (String) event.get("courseSubject");
    int CRN = (int) event.get("CRN");

    //! this is extra info for the class --> should be getting this from the input (event)
    final Map<String, Object> extraClassInfo = new HashMap<String, Object>();
    extraClassInfo.put("NumOfSeats", 20);
    extraClassInfo.put("Instructor", "Dr. Jigar Patel");
    
    //! fixme: shouldn't have to pass in client as argument here
    // * use the db ref to make entry to database
    client.makeDatabaseEntry(subject, CRN, client, extraClassInfo);

  }

  @Override
  public String handleRequest(Map<String, Object> event, Context context) {
    LambdaLogger logger = context.getLogger();

    // ! error message should be thrown --> so that it can be displayed
    String response = new String("200 OK??");
    
    
    // * next thing I need to do is take some actual input (courseSubject, CRN)  --> parse it for db entry
    //* here I will need to write some code to interface w/ dynamoDB

    writeToDatabase(event);

  
    // process event
    logger.log("EVENT: " + gson.toJson(event));
  
    return response;
  }
}