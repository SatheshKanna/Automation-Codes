package Samsung;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import logsAndExceptionWriter.WriteLogsAndExceptions;

public class Base64Encoder 
{
	public static String parameters;
	
	public static String encode(JSONObject jsonObject) throws Exception
	{
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Encoding the Json object parameters with Base64");
		try
		{
			//Encode the JsonObject to be passed to the API
			String Test = jsonObject.toString();
			byte[] encodedBytes = Base64.encodeBase64(Test.getBytes());
			//System.out.println("encodedBytes=" + new String(encodedBytes));
			parameters = new String(encodedBytes);
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Base64 encoded parameter is: " + parameters);
			return parameters;
		}
		catch(Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
			return null;
		}
	}
}
