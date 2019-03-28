package Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import logsAndExceptionWriter.WriteLogsAndExceptions;

public class HTMLWriter 
{
	public static void writeHTML(String OutputHTMLPath, String content) throws Exception
	{
		//Delete the Old HTML output file
		File oldOutputFile = new File(OutputHTMLPath);
		if(oldOutputFile.exists())
		{
			oldOutputFile.delete();
		}
		//Write the HTML file with the new content
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try 
		{
		    fWriter = new FileWriter(OutputHTMLPath);
		    
		    writer = new BufferedWriter(fWriter);
		    writer.write(content);
		    writer.close(); 
		} 
		catch (Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
}
