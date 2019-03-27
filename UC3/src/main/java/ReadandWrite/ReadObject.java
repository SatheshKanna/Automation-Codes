package ReadandWrite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import TestExecute.ExecuteTest;
public class ReadObject 
{
    Properties p = new Properties();
    public Properties getObjectRepository() throws IOException
    {
        //Read object repository file
        //InputStream stream = new FileInputStream(new File(System.getProperty("user.dir")+"\\UC3Objects\\object.properties"));
    	InputStream stream = new FileInputStream(new File(ExecuteTest.ConfigFilePath));
        //load all objects
        p.load(stream);
        return p;
    }
    
}