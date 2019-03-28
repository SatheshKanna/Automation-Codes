package Samsung;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import logsAndExceptionWriter.WriteLogsAndExceptions;

public class GetResponse 
{
	public static String data;
	
	public static String getData(String JSONparameters) throws Exception
	{
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Getting the Response Data");
		try
		{
			//Append the URL to be sent to the API with the parameters 
			String AppendedURL = MainClass.APIUrl + JSONparameters;
			//Get the Response
			Response resp = RestAssured.get(AppendedURL);
			data = resp.asString();
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Response Data is: " + data);
			return data;
		}
		catch (Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
			return null;
		}
	}
}
