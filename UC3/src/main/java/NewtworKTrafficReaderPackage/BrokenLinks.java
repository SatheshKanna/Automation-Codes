package NewtworKTrafficReaderPackage;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import logsAndExceptionWriter.WriteLogsAndExceptions;

public class BrokenLinks 
{
	static HttpURLConnection huc = null;
    public static int respCode;
	public static int isLinkBroken(String Url) throws Exception
	{
		respCode = 0;
		try 
		{
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"Checking the Url for Broken Link");
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            huc = (HttpURLConnection)(new URL(Url).openConnection());
            huc.setRequestMethod("GET");
            huc.connect();
            respCode = huc.getResponseCode();
            huc.disconnect();
            //return respCode;
        } 
		catch (MalformedURLException e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
        } 
		catch (IOException e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
        }
		return respCode;
	}

}
