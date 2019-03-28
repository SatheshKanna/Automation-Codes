package Utility;

import java.io.File;
import java.io.FileWriter;

import logsAndExceptionWriter.WriteLogsAndExceptions;

public class writeJson 
{
	public static void Write(String ListTaskJsonFilePath, String Response) throws Exception
	{
		File ListTaskJsonFile = new File(ListTaskJsonFilePath);
		if(ListTaskJsonFile.exists())
		{
			ListTaskJsonFile.delete();
		}
		try (FileWriter file = new FileWriter(ListTaskJsonFilePath)) 
		{
			 
            file.write(Response.toString());
            file.flush();
 
        } 
		catch (Exception e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
        }
	}
}
