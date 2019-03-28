package Utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import logsAndExceptionWriter.WriteLogsAndExceptions;

public class WriteTextFile 
{
	public static void WriteText(String FilePath, String value) throws Exception
	{
		try(FileWriter fw = new FileWriter(FilePath, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw))
		{
			out.println(value);
		}
		catch (Exception e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
}
