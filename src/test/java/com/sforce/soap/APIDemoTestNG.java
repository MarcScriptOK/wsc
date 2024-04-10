package com.sforce.soap;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import utils.PropertyFileReader;
import utils.SalesforceManager;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.fail;

public class APIDemoTestNG {
    private static ConnectorConfig credentials = new ConnectorConfig();
    private static EnterpriseConnection connection;
    private static SalesforceManager salesforceManager;
    //Setting variables
    static String leadFirstName;
    static String leadLastName;
    static String leadCompany;
    static String leadIndustry;
    static Properties prop = new Properties();

    @BeforeClass
    public static void setUp() {
        final String USERNAME = System.getenv("-SFUsername");
        final String PASSWORD = System.getenv("-SFPassword");
        credentials.setUsername(USERNAME);
        credentials.setPassword(PASSWORD);
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            leadFirstName = prop.getProperty("LEAD_LAST_NAME");
            leadLastName = prop.getProperty("LEAD_LAST_NAME");
            leadCompany = prop.getProperty("LEAD_COMPANY");
            leadIndustry = prop.getProperty("LEAD_INDUSTRY");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            connection = Connector.newConnection(credentials);
            salesforceManager = new SalesforceManager(connection);
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

    @Test(priority = 1)
    public void testCreateLead() {
        try {
            //Lead creation
            Lead lead = new Lead();
            lead.setFirstName(leadFirstName);
            lead.setLastName(leadLastName);
            lead.setCompany(leadCompany);
            lead.setIndustry(leadIndustry);
            List<String> fields = Arrays.asList("Id", "FirstName", "LastName");
            //Salesforce's connection (Create Record)
            String leadId = salesforceManager.createRecord(lead);

            //Salesforce connection (Query)
            Lead createdLead = (Lead) salesforceManager.getRecordById("Lead", leadId,fields);
            Assert.assertNotNull(createdLead, "Lead record was not created");
            Assert.assertEquals(createdLead.getLastName(), leadLastName, "Lead record has incorrect last name");
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 2)
    public void testUpdateLead() {
        try {
            List<String> fields = Arrays.asList("Id");
            // Search for existing Lead
            Lead lead = (Lead) salesforceManager.getRecordByField("Lead", "LastName", leadLastName,fields);
            Assert.assertNotNull(lead, "Lead record to update not found");

            // Modify the created lead
            lead.setCompany("New Company");

            // Update the lead
            boolean isSuccess = salesforceManager.updateRecord(lead);
            Assert.assertTrue(isSuccess, "Lead record was not updated");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 3)
    public void testQueryLead() {
        try {
            String soqlQuery = "SELECT Id, FirstName, LastName, Company, CreatedDate FROM Lead ORDER BY CreatedDate DESC LIMIT 10";
            //Salesforce connection (Query)
            QueryResult qr = connection.query(soqlQuery);
            //Show the results on screen
            salesforceManager.printRecords(qr);
        } catch (ConnectionException e) {
            e.printStackTrace();
            fail("Failed to execute query via API");
        }
        System.out.println("\nQuery execution completed.");
    }

    @Test(priority = 4)
    public void testDeleteLead() throws ConnectionException {
        try {
            //Salesforce connection (Query)
            List<String> fields = Arrays.asList("Id");
            Lead lead = (Lead) salesforceManager.getRecordByField("Lead", "LastName", leadLastName,fields);
            Assert.assertNotNull(lead, "Lead record to delete not found");

            //Delete verification
            boolean isSuccess = salesforceManager.deleteRecord("Lead", lead.getId());
            Assert.assertTrue(isSuccess, "Lead record was not deleted");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
