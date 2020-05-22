package example.Models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Map;

@DynamoDBTable(tableName = "uicCoursesFall2020")
public class uicCoursesModel {

    @DynamoDBHashKey(attributeName = "courseName")
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @DynamoDBRangeKey(attributeName = "CRN")
    public int getCRN() {
        return CRN;
    }
    public void setCRN(int CRN) {
        this.CRN = CRN;
    }

    @DynamoDBIgnore
    public Map<String, Object> getExtraClassInfo() {
        return extraClassInfo;
    }

    private String courseName;
    private int CRN;
    final Map<String, Object> extraClassInfo;


    public uicCoursesModel(String courseName, int crn, Map<String, Object> extraClassInfo) {
        this.courseName = courseName;
        CRN = crn;
        this.extraClassInfo = extraClassInfo;
    }

    //* add getter & setter for courseName
}
