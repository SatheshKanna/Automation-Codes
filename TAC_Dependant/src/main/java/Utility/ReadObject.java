package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import Samsung.MainClass;
import logsAndExceptionWriter.WriteLogsAndExceptions;
public class ReadObject 
{
    Properties p = new Properties();
    public Properties getObjectRepository() throws Exception
    {
    	try
    	{
	        //Read Config File to get all the config values
    		//InputStream stream = new FileInputStream(new File("C:\\Users\\sathesh.gunasekaran\\workspace\\APITesting\\Config\\config.properties"));
	    	InputStream stream = new FileInputStream(new File(MainClass.ConfigFilePath));
	        p.load(stream);
	        return p;
    	}
    	catch (Exception e)
    	{
    		WriteLogsAndExceptions.appendToFile(e);
    		return null;
    	}
    }
}