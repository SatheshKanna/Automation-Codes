package ITP;

import java.io.File;

public class sendEmailNoSMTP
{

	public static void runBAT(String NoSMTPEmailBatFileName, String NoSMTPEmailBatFilePath) throws Exception
   {
	   try
	   {
		   //Execute the VB script by processing the Bat file to send the Email
		   ProcessBuilder pb = new ProcessBuilder("cmd", "/c", NoSMTPEmailBatFileName);
		   File dir = new File(NoSMTPEmailBatFilePath);
		   pb.directory(dir);
		   Process p = pb.start();
		   p.waitFor();
	   }
	   catch (Exception e) 
	   {
		   e.printStackTrace();
	   }
   }
}