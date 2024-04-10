package com.sforce.soap;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.EnterpriseConnection;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.Error;
import com.sforce.ws.*;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class APIDemoJunit {
    private static ConnectorConfig credentials = new ConnectorConfig();
    private static EnterpriseConnection connection;
    //Setting variables
    private static final String leadLastName = "Oktana API Test";
    private static final String leadCompany = "Oktana Push";
    private static final String leadIndustry = "IT";
    @BeforeClass
    public static void setUp() {
        final String USERNAME = System.getenv("-SFUsername");
        final String PASSWORD = System.getenv("-SFPassword");
        credentials.setUsername(USERNAME);
        credentials.setPassword(PASSWORD);
        try {
            connection = Connector.newConnection(credentials);
        } catch (ConnectionException e) {
            e.printStackTrace();
            fail("Failed to establish connection with Salesforce");
        }
    }

    @AfterClass
    public static void tearDown() {
        if (connection != null) {
            try {
                connection.logout();
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testACreateStandardLeadFromAPI() {
        try {
            //Lead creation
            Lead lead = new Lead();
            lead.setLastName(leadLastName);
            lead.setCompany(leadCompany);
            lead.setIndustry(leadIndustry);

            //Salesforce's connection (Create Record)
            connection.create(new Lead[]{lead});

            //Salesforce connection (Query)
            String soqlQuery = "SELECT Id, LastName FROM Lead WHERE LastName=\'"+leadLastName+ "\' LIMIT 1";
            QueryResult qr = connection.query(soqlQuery);
            SObject[] records = qr.getRecords();
            Lead createdLead = (Lead) records[0];
            String Id = createdLead.getId();
            System.out.println("Lead ID = " + Id);
            Assert.assertNotNull("Object Id was not created", Id);
            Assert.assertTrue("Object Id was not created", records.length > 0);
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBDeleteRecord() throws ConnectionException {
        //Salesforce connection (Query)
        String soqlQuery = "SELECT Id, LastName FROM Lead WHERE LastName=\'"+leadLastName+ "\' LIMIT 1";
        QueryResult qr = connection.query(soqlQuery);
        SObject[] records = qr.getRecords();
        Lead createdLead = (Lead) records[0];
        String Id = createdLead.getId();

        //Delete verification
        DeleteResult[] deleteResults = connection.delete(new String[]{Id});
        for (int i = 0; i < deleteResults.length; i++) {
            DeleteResult deleteResult = deleteResults[i];
            if (deleteResult.isSuccess()) {
                System.out.println("Deleted Record ID: " + deleteResult.getId());
            } else {
                // Handle the errors.
                // We just print the first error out for sample purposes.
                Error[] errors = deleteResult.getErrors();
                if (errors.length > 0) {
                    System.out.println("Error: could not delete " + "Record ID "
                            + deleteResult.getId() + ".");
                    System.out.println("   The error reported was: ("
                            + errors[0].getStatusCode() + ") "
                            + errors[0].getMessage() + "\n");
                }
            }
        }
    }

    @Test
    public void testCQuerySample() {
        try {
            String soqlQuery = "SELECT FirstName, LastName FROM Contact";
            //Salesforce connection (Query)
            QueryResult qr = connection.query(soqlQuery);
            System.out.println("Number of results retrieved: " + qr.getSize());
            //Show the results on screen
            SObject[] records = qr.getRecords();
            for (int i = 0; i < records.length; i++) {
                Contact contact = (Contact) records[i];
                Object firstName = contact.getFirstName();
                Object lastName = contact.getLastName();
                System.out.println("Contact " + (i + 1) + ": " + firstName + " " + lastName);
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
            fail("Failed to execute query via API");
        }
        System.out.println("\nQuery execution completed.");
    }
}

