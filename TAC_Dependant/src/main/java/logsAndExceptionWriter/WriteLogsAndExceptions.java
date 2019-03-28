package logsAndExceptionWriter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import Utility.ReadObject;

public class WriteLogsAndExceptions 
{
	
	public static void appendToFile(Exception e) throws Exception 
	{
		ReadObject objects = new ReadObject();
    	Properties allObjects =  objects.getObjectRepository();
    	String ExceptionsPath = allObjects.getProperty("ExceptionsPath");
		String ExceptionsFile = ExceptionsPath + "\\Exceptions.txt";
	    
		FileWriter fstream = new FileWriter(ExceptionsFile, true);
	    BufferedWriter out = new BufferedWriter(fstream);
	    PrintWriter pWriter = new PrintWriter(out, true);
	      try 
	      {
	    	  e.printStackTrace();
	    	  pWriter.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + e.getMessage());
	    	  pWriter.println("");
	      }
	      catch (Exception ie) 
	      {
	         throw new RuntimeException("Could not write Exception to file", ie);
	      }
	      finally
	      {
	    	  pWriter.flush();
	    	  out.close();
	    	  fstream.close();
	      }
	}
	
	public static void writeLogs(String LogsFile) throws Exception
	{
		PrintStream out;
		try 
		{
			out = new PrintStream(new FileOutputStream(LogsFile, true));
			System.setOut(out);
		} 
		catch (Exception e)
		{
			appendToFile(e);
		}
	}
}
