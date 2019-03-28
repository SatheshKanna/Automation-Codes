package Utility;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logsAndExceptionWriter.WriteLogsAndExceptions;

@SuppressWarnings("unchecked")
public class UpdateJson 
{
	public static JSONObject writeJsonObject;
	
	public static Object setTaskID(String TaskID, String FilePath) throws Exception
	{
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Setting the Task ID in the Json file");
		try 
		{
			//Update the Task ID in the Json file
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(FilePath));
			writeJsonObject = (JSONObject) obj;
			writeJsonObject.remove("taskId");
			writeJsonObject.put("taskId", TaskID);
			return writeJsonObject;
		} 
		catch (Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
			return null;
		}
	}
	
	
	public static Object parseJson(String FilePath) throws Exception
	{
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Parsing the Json file");
		try 
		{
			//Update the Task ID in the Json file
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(FilePath));
			writeJsonObject = (JSONObject) obj;
			return writeJsonObject;
		} 
		catch (Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
			return null;
		}
	}
	
	
	public static Object setRunPlanJson(String ExePlanID, String NodeID, String FilePath) throws Exception
	{
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Setting the Execution Plan ID and Node ID in the Json file");
		try 
		{
			//Update the Task ID in the Json file
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(FilePath));
			writeJsonObject = (JSONObject) obj;
			writeJsonObject.remove("planId");
			writeJsonObject.put("planId", ExePlanID);
			writeJsonObject.remove("planPartId");
			writeJsonObject.put("planPartId", NodeID);
			return writeJsonObject;
		} 
		catch (Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
			return null;
		}
	}
}
