package Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import logsAndExceptionWriter.WriteLogsAndExceptions;

public class ReadHTMLTemplate 
{
   public static String content;
   
   public static void attachHTMLasString(String OututHTMLpath, String HTMLReportTable) throws Exception 
   {
	   //Read all the HTML Template File Content
	   StringBuilder contentBuilder = new StringBuilder();
	   try 
	   {
	       BufferedReader in = new BufferedReader(new FileReader(OututHTMLpath));
	       String str;
	       while ((str = in.readLine()) != null) 
	       {
	           contentBuilder.append(str);
	       }
	       in.close();
	   } 
	   catch (Exception e) 
	   {
		   WriteLogsAndExceptions.appendToFile(e);
	   }
	   //Store the HTML file content in a String
	   String actualTemplateContent = contentBuilder.toString();
	   //Replace the ReportTable word by the Code generated Table or Content
	   content = actualTemplateContent.replaceAll("REPORT_TABLE", HTMLReportTable);
	   System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The HTML Table is: "+ content);
   }
}
