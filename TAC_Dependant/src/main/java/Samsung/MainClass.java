package Samsung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Samsung.Base64Encoder;
import Samsung.GetResponse;
import Utility.HTMLWriter;
import Utility.ReadHTMLTemplate;
import Utility.ReadObject;
import Utility.SendEmailTLS;
import Utility.UpdateJson;
import Utility.sendEmailNoSMTP;
import Utility.writeJson;
import logsAndExceptionWriter.FileZipperForLogsandException;
import logsAndExceptionWriter.WriteLogsAndExceptions;

public class MainClass 
{
	private static final String COMMA_DELIMITER = ",";
	public static String ConfigFilePath;
	static String TaskName;
	static String TaskID;
	static String TaskErrorStatus;
	static String ReRunError;
	static String APIUrl;
	static String InputFilePath;
	static String AllTaskStatusJsonPath;
	static String ListTasksPath;
	static String HTMLTableHeader;
	static String HTMLTableRows;
	static String ReRunStatus;
	static int SleepSeconds;
	static String OutputHTMLTemplate;
	static String BatFilePath;
	static String BatFileName;
	static String OutputHTMLPath;
	static String ExecutionPlanID;
	static String TaskNode;
	static String RunAs;
	static String getTaskStatusJsonPath;
	static String runPlanFromNodeJsonPath;
	static String mail;
	
	public static void main(String[] args) throws Exception
	{
		//Config File Path
		InputFilePath = args[0];
		String configFile = args[1];
//		String configFile = "C:\\Users\\sathesh.gunasekaran\\workspace\\TAC_Dependant\\Config\\config.properties";
//		InputFilePath = "C:\\Users\\sathesh.gunasekaran\\workspace\\TAC_Dependant\\Input\\Input.csv";
    	ConfigFilePath = configFile;
    	
    	//Read Config File to get all the respective values
    	ReadObject objects = new ReadObject();
    	Properties allObjects =  objects.getObjectRepository();
    	String LogsPath = allObjects.getProperty("LogsPath");
    	File LogsDirectory = new File(LogsPath);
		if (! LogsDirectory.exists())
		{
			LogsDirectory.mkdir();
	    }
        String ExceptionsPath = allObjects.getProperty("ExceptionsPath");
        File ExceptionsDirectory = new File(ExceptionsPath);
        if (! ExceptionsDirectory.exists())
		{
        	ExceptionsDirectory.mkdir();
	    }
        
        String deleteAfter = allObjects.getProperty("deleteAfter");
        ListTasksPath = allObjects.getProperty("ListTasksPath");
        AllTaskStatusJsonPath = allObjects.getProperty("AllTaskStatusJsonPath");
        getTaskStatusJsonPath=allObjects.getProperty("getTaskStatusJsonPath");
        runPlanFromNodeJsonPath=allObjects.getProperty("runPlanFromNodeJsonPath");
        APIUrl = allObjects.getProperty("APIUrl");
        HTMLTableHeader = allObjects.getProperty("HTMLTableHeader");
        String SleepInMilliSecond = allObjects.getProperty("SleepInMilliSecond");
        SleepSeconds = Integer.parseInt(SleepInMilliSecond);
        OutputHTMLTemplate = allObjects.getProperty("OutputHTMLTemplate");
	    BatFilePath = allObjects.getProperty("BatFilePath");
	    BatFileName = allObjects.getProperty("BatFileName");
        OutputHTMLPath = allObjects.getProperty("OutputHTMLPath");
        mail = allObjects.getProperty("mail");
        String LogsFile = LogsPath + "\\Logs.txt";
        String ZiplogsFile = LogsPath + "\\Logs" + new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(new Date()) + ".zip";
        String ZippedLogs = LogsPath + "\\Backup Logs";
    	String ZipExceptionsFile = ExceptionsPath + "\\Exceptions" + new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(new Date()) + ".zip";
        String ZippedExceptions = ExceptionsPath + "\\Backup Exceptions";
        String LogsFolder = "In Logs Folder";
        String ExceptionsFolder = "In Exceptions Folder";
        int maxDays = Integer.parseInt(deleteAfter);
        
		FileZipperForLogsandException.mainFileZipper(LogsFolder,LogsPath,ZiplogsFile,ZippedLogs,maxDays);
		FileZipperForLogsandException.mainFileZipper(ExceptionsFolder,ExceptionsPath,ZipExceptionsFile,ZippedExceptions,maxDays);
		
		//Write all logs in Text File
		WriteLogsAndExceptions.writeLogs(LogsFile);
		
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Code is Started");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Writing all the console logs in the Logs Text File");
		BufferedReader reader;
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Reading the Input Text File");
		try 
		{
			//List all the task to get the current status
			ListTasks();
			//Read the Input CSV
			reader = new BufferedReader(new FileReader(InputFilePath));
			String job = "";
            //Read to skip the header
			reader.readLine();
            //Reading from the second line
            while ((job = reader.readLine()) != null) 
            {
				TaskID="";
				TaskErrorStatus="";
				ReRunError="";
				ReRunStatus="";
				ExecutionPlanID="";
				TaskName="";
				TaskNode="";
				RunAs="";
				
				String[] jobArray = job.split(COMMA_DELIMITER);
                
                if(jobArray.length > 0 )
                {
                    ExecutionPlanID = jobArray[1];
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Execution Plan ID of the Task is: " + ExecutionPlanID);
                    TaskName = jobArray[2];
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Name of the Task is: " + TaskName);
                    TaskNode = jobArray[3];
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Node of the Task in the Execution plan is: " + TaskNode);
                    RunAs = jobArray[4];
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Run the Task as: " + RunAs);
					JSONParser parser = new JSONParser();
					Object obj = parser.parse(new FileReader(AllTaskStatusJsonPath));
		            JSONObject setupObject = (JSONObject) obj;
		            JSONArray setup = (JSONArray) setupObject.get("result");
		            for (int i = 0; i < setup.size(); i++) 
		            {
		            	String label = ((JSONObject) setup.get(i)).get("label").toString();
		            	if(label.equalsIgnoreCase(TaskName))
		            	{
		            		TaskErrorStatus = ((JSONObject) setup.get(i)).get("errorStatus").toString();
		            		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Error Status of the Task - "+TaskName+" is: " + TaskErrorStatus);
		    				//Dont run the Task if the Task has "No_Error"
		    				if(TaskErrorStatus.equalsIgnoreCase("JOB_ERROR") || TaskErrorStatus.equalsIgnoreCase("UNKNOWN"))
		    				{
		    					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "So we need to Re-run the Task - "+TaskName+ " of the Execution Plan from its node");
		    					TaskID = ((JSONObject) setup.get(i)).get("id").toString();
		    					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Task ID is: " + TaskID);
		    					runPlanFromNode(ExecutionPlanID,TaskNode, TaskName, TaskID);
		    				}
		    				else
		    				{
		    					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Task - "+TaskName+" has No_Error, so no need to re-run the task");
		    				}
		            	}
		            }
	            }
			}
			reader.close();
			String HTMLReportTable = HTMLTableHeader + "</table>";
			//Replace HTML Report Table only when there are any rows
			if(HTMLReportTable.contains("<tr><td>"))
			{
				ReadHTMLTemplate.attachHTMLasString(OutputHTMLTemplate, HTMLReportTable);
				HTMLWriter.writeHTML(OutputHTMLPath, ReadHTMLTemplate.content);
				if(mail.equalsIgnoreCase("server"))
				{
					SendEmailTLS.sendEmail(ReadHTMLTemplate.content);
				}
				else if(mail.equalsIgnoreCase("local"))
				{
					sendEmailNoSMTP.runBAT(BatFileName, BatFilePath);
				}
				else
				{
					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Mailing Option is not specified correctly");
				}
			}
			//Replace HTML Report Table as there are no error jobs
//			else
//			{
//				ReadHTMLTemplate.attachHTMLasString(OutputHTMLTemplate, WhenNoTableBody);
//			}
			
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Code is Ended");
		}
		catch(Exception e)
		{			
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
	public static void ListTasks() throws Exception
	{
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Listing all the Task");
		try
		{
			UpdateJson.parseJson(ListTasksPath);
			Base64Encoder.encode(UpdateJson.writeJsonObject);
			String JSONparameters = Base64Encoder.parameters;
			//Get the API response
			GetResponse.getData(JSONparameters);
			JSONParser parse = new JSONParser();
			JSONObject myObject = (JSONObject)parse.parse(GetResponse.data);
			//Get the returnCode of the response
			String ListTaskReturnCode = myObject.get("returnCode").toString();
			if(ListTaskReturnCode.equalsIgnoreCase("0"))
			{
				writeJson.Write(AllTaskStatusJsonPath, GetResponse.data);
			}
			
		}
		catch(Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
	
	public static void getTaskStatus(String TName, String TID) throws Exception
	{
		try 
		{
			//Update the Json Parameter with the Task Execution ID
			UpdateJson.setTaskID(TID, getTaskStatusJsonPath);
			//Encode Json Parameter to Base64
			Base64Encoder.encode(UpdateJson.writeJsonObject);
			String JSONparameters = Base64Encoder.parameters;
			//Get the API response
			GetResponse.getData(JSONparameters);
			JSONParser parse = new JSONParser();
			JSONObject myObject = (JSONObject)parse.parse(GetResponse.data);
			//Get the returnCode of the response
			JSONParser parse1;
			JSONObject myObject1;
			String getStatusReturnCode = myObject.get("returnCode").toString();
			if(getStatusReturnCode.equalsIgnoreCase("0"))
			{
				String getStatus  = myObject.get("status").toString();
				String errorStatus = myObject.get("errorStatus").toString();
				while (getStatus.equalsIgnoreCase("RUNNING") || getStatus.equalsIgnoreCase("REQUESTING_RUN"))
				{
					Thread.sleep(SleepSeconds);
					GetResponse.getData(JSONparameters);
					parse1 = new JSONParser();
					myObject1 = (JSONObject)parse1.parse(GetResponse.data);
					getStatus = myObject1.get("status").toString();
					errorStatus = myObject1.get("errorStatus").toString();
					Thread.sleep(10000);
				}
				if(errorStatus.equalsIgnoreCase("NO_ERROR") && getStatus.equalsIgnoreCase("READY_TO_RUN"))
				{
					ReRunStatus = "PASS";
					ReRunError = errorStatus;
					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The task- "+TName+ " run was SUCCESSFUL" );
				}
				else
				{
					ReRunStatus = "FAIL";
					ReRunError = errorStatus;
					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The task- "+TName+ " run was UNSUCCESSFUL" );
				}
			}
			else 
			{
				ReRunStatus = "FAIL";
				ReRunError = myObject.get("error").toString();
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The task- "+TName+ " run was UNSUCCESSFUL" );
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Error while getting the Execution Status");
			}
		}
		catch(Exception e)
		{
			ReRunStatus = "FAIL";
			ReRunError = "Exception while getting the Execution Status";
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The task- "+TName+ " run was UNSUCCESSFUL" );
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Exception while getting the Execution Status");
		}
	}
	public static void runPlanFromNode(String ExePlanID, String NodeID, String taskName, String taskID) throws Exception
	{
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Running the Task");
		try
		{
			//Update the Json Parameter with the Task ID
			UpdateJson.setRunPlanJson(ExePlanID,NodeID, runPlanFromNodeJsonPath);
			//Encode Json Parameter to Base64
			Base64Encoder.encode(UpdateJson.writeJsonObject);
			String JSONparameters = Base64Encoder.parameters;
			//Get the API response
			GetResponse.getData(JSONparameters);
			JSONParser parse = new JSONParser();
			JSONObject myObject = (JSONObject)parse.parse(GetResponse.data);
			//Get the returnCode of the response
			String ReturnCode = myObject.get("returnCode").toString();
			//Proceed only when the response we got is Success == 0
			if(ReturnCode.equalsIgnoreCase("0"))
			{
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Started the "+taskName+ " to run" );
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Getting the "+taskName+ " status" );
				getTaskStatus(taskName, taskID);
				
			}
			else
			{
				ReRunStatus = "FAIL";
				ReRunError = myObject.get("error").toString();
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Error while running the task");
			}
		}
		catch (Exception e)
		{
			ReRunStatus = "FAIL";
			ReRunError = "Exception while running the task";
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Exception while running the task");
		}
		if(ReRunStatus.equalsIgnoreCase("PASS"))
		{
			String jobDetails[] = {taskName,ReRunStatus,ReRunError};
			HTMLTableRows="<tr><td>"+jobDetails[0]+"</td><td>"+jobDetails[1]+"</td><td>"+jobDetails[2]+"</td></tr>";
			HTMLTableHeader = HTMLTableHeader + HTMLTableRows;
		}
	}
}
