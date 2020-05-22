package example.Handlers;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;

import example.DynamoDB.DynamoDBClientFactory;

import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;


// * what are the steps to turn this into a java lambda function
public class GetItem implements RequestHandler<Map<String, String> , JSONObject> {


    private JSONObject searchItem(int CRN){

        //* set AWS profile
        String AWSprofile =  "default";
        String tableName = "uicCoursesFall2020";
    
        // * call the class to perform write to DB (class constructor needs region & profile, and tableName)
        DynamoDBClientFactory client = new DynamoDBClientFactory(Regions.US_EAST_1, AWSprofile, tableName);

        return client.searchClass(CRN);

    }

    @Override
    public JSONObject handleRequest( Map<String, String> event, Context context) {
        //* getting query params using api gateway
        int CRN = Integer.parseInt(event.get("CRN"));

        return searchItem(CRN);
    }

}