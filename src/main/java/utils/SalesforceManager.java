package utils;

import com.sforce.soap.enterprise.*;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;

import java.util.List;

public class SalesforceManager {
    private final EnterpriseConnection connection;

    public SalesforceManager(EnterpriseConnection connection) {
        this.connection = connection;
    }

    public String createRecord(SObject record) throws ConnectionException {
        SaveResult[] saveResults = connection.create(new SObject[]{record});
        return saveResults.length > 0 ? saveResults[0].getId() : null;
    }

    public boolean updateRecord(SObject record) {
        try {
            SaveResult[] saveResults = connection.update(new SObject[]{record});
            return saveResults.length > 0 && saveResults[0].isSuccess();
        } catch (ConnectionException e) {
            throw new RuntimeException("Failed to update record", e);
        }
    }

    public boolean deleteRecord(String objectType, String recordId) throws ConnectionException {
        DeleteResult[] deleteResults = connection.delete(new String[]{recordId});
        return deleteResults.length > 0 && deleteResults[0].isSuccess();
    }

    public SObject getRecordById(String objectType, String recordId, List<String> fields) throws ConnectionException {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        for (String field : fields) {
            queryBuilder.append(field).append(", ");
        }
        String query = queryBuilder.substring(0, queryBuilder.length() - 2) + " FROM " + objectType + " WHERE Id = '" + recordId + "'";
        QueryResult qr = connection.query(query);
        SObject[] records = qr.getRecords();
        return records.length > 0 ? records[0] : null;
    }

    public SObject getRecordByField(String objectType, String fieldName, String fieldValue, List<String> fieldsToRetrieve) throws ConnectionException {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        for (String field : fieldsToRetrieve) {
            queryBuilder.append(field).append(", ");
        }
        String query = queryBuilder.substring(0, queryBuilder.length() - 2) + " FROM " + objectType + " WHERE " + fieldName + " = '" + fieldValue + "' LIMIT 1";
        QueryResult qr = connection.query(query);
        SObject[] records = qr.getRecords();
        return records.length > 0 ? records[0] : null;
    }

    private Field[] getObjectFields(String objectType) throws ConnectionException {
        DescribeSObjectResult describeSObjectResult = connection.describeSObject(objectType);
        return describeSObjectResult.getFields();
    }

    public void printRecords(QueryResult queryResult) {
        System.out.println("Number of results retrieved: " + queryResult.getSize());
        SObject[] records = queryResult.getRecords();
        for (int i = 0; i < records.length; i++) {
            System.out.println("Record #" + (i + 1) + ": " + records[i].getId());
        }
    }
}
