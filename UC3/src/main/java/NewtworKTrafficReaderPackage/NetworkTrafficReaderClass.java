package NewtworKTrafficReaderPackage;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import JavascriptConverter.JSReader;
import ReadandWrite.ReadObject;
import TestExecute.ExecuteTest;
import logsAndExceptionWriter.WriteLogsAndExceptions;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;


public class NetworkTrafficReaderClass 
{
	
	static String FrameworkInputFile = ExecuteTest.FrameworkInputFile;
	
	public static BrowserMobProxy proxy;
	public static Proxy seleniumProxy;
	static Workbook wb;
	static Sheet sh1;
	static Row row;
	static String Url;
	public static String URL1;
	
	public static void proxySetUp() throws Exception
	{
		try
		{
		    proxy = new BrowserMobProxyServer();
		    proxy.start(0);
		    seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		}
		catch(Exception e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
	
	public static void inputExcelReader() throws Exception
	{
		try
		{
			File src=new File(FrameworkInputFile);
			FileInputStream fis=new FileInputStream(src);
			wb = new XSSFWorkbook(fis);
			sh1= wb.getSheetAt(0);
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"Reading the Input Excel File");
		}
		catch(Exception e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
	@SuppressWarnings("unused")
	public static void UrlandLoadEventCheck(WebDriver driver) throws Exception
	{
		try
		{
			row = sh1.getRow(1);
		    Url="";
			String LoadEvent="";
		    if(row.getCell(0)!=null &&  !row.getCell(0).getStringCellValue().isEmpty() && row.getCell(0).getStringCellValue()!="") 
		    {
		    	Url = row.getCell(0).getStringCellValue();
		    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Working on URL at the Cell ["+1+","+0+"] : " + Url);
	    		if(row.getCell(1)!=null && !row.getCell(1).getStringCellValue().isEmpty() && row.getCell(1).getStringCellValue()!="" && row.getCell(1).getStringCellValue().equalsIgnoreCase("Yes"))
	    		{
	    			URL1 = Url.toLowerCase();
	    			LoadEvent = row.getCell(1).getStringCellValue();
	    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"We need to capture the Network Traffic for Load Event as the Load Event is mentioned YES at the Cell ["+1+","+1+"]");
		    		Thread.sleep(5000);
		    		JavascriptExecutor js = (JavascriptExecutor)driver;
		    		if (js.executeScript("return document.readyState").toString().equals("complete"))
		    		{
		    			Thread.sleep(5000);
			    		harFileDownloader(driver);
		    		}
	    		}
	    		else
	    		{
	    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The mentioned LoadEvent status is Wrong or Empty or Not-Required at the Cell ["+1+","+1+"]");
	    		}
		    }
		    else
		    {
		    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The Url is wrong or empty");
		    }
		}
		catch(Exception e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
		@SuppressWarnings("unused")
		public static void ClickEventCheck(WebDriver driver) throws Exception
		{
			try
			{
				String ActivityName="";
				String ClickEvent="";
				String LocatorType="";
				String Locator="";
				WebDriverWait wait =  new WebDriverWait(driver, 30);
				if(row.getCell(2)!=null &&  !row.getCell(2).getStringCellValue().isEmpty() && row.getCell(2).getStringCellValue()!="" && row.getCell(2).getStringCellValue().equalsIgnoreCase("Yes") )
	    		{
	    			ClickEvent = row.getCell(2).getStringCellValue();
	    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"We need to capture the Network Traffic for Click Event as the Click is mentioned as YES at the Cell ["+1+","+2+"]");
	    			
	    		    if(row.getCell(5)!=null && !row.getCell(5).getStringCellValue().isEmpty() && row.getCell(5).getStringCellValue()!="")
	    		    {
	    		    	ActivityName = row.getCell(5).getStringCellValue();
	    		    	URL1 = (Url+ "|" + ActivityName).toLowerCase();
	    		    	Thread.sleep(5000);
			    		if(row.getCell(3)!=null && !row.getCell(3).getStringCellValue().isEmpty() && row.getCell(3).getStringCellValue()!="" && row.getCell(4)!=null && !row.getCell(4).getStringCellValue().isEmpty() && row.getCell(4).getStringCellValue()!="")
			    		{
			    			LocatorType = row.getCell(3).getStringCellValue();
			    			Locator = row.getCell(4).getStringCellValue();
					    	if (LocatorType.equalsIgnoreCase("XPath"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locator)));
					    		try 
					    		{
					    		Thread.sleep(500);
					    		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Locator)));
						    	driver.findElement(By.xpath(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
						    	{
						    		Thread.sleep(5000);
							    	harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	else if (LocatorType.equalsIgnoreCase("ClassName"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(Locator)));
					    		try
					    		{
				    			wait.until(ExpectedConditions.elementToBeClickable(By.className(Locator)));
						    	driver.findElement(By.className(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
						    	{
						    		Thread.sleep(5000);
						    		harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	else if (LocatorType.equalsIgnoreCase("Name"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(Locator)));
					    		try
					    		{
					    		wait.until(ExpectedConditions.elementToBeClickable(By.name(Locator)));
						    	driver.findElement(By.name(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
						    	{
						    		Thread.sleep(5000);
						    		harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	else if (LocatorType.equalsIgnoreCase("CSS"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(Locator)));
					    		try
					    		{
					    		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(Locator)));
						    	driver.findElement(By.cssSelector(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
						    	{
						    		Thread.sleep(5000);
						    		harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	else if (LocatorType.equalsIgnoreCase("LinkText"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(Locator)));
					    		try
					    		{
					    		wait.until(ExpectedConditions.elementToBeClickable(By.linkText(Locator)));
						    	driver.findElement(By.linkText(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
					    		{
						    		Thread.sleep(5000);
						    		harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	else if (LocatorType.equalsIgnoreCase("PartialLinkText"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(Locator)));
					    		try
					    		{
					    		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(Locator)));
						    	driver.findElement(By.partialLinkText(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
						    	{
						    		Thread.sleep(5000);
						    		harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	else if (LocatorType.equalsIgnoreCase("ID"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(Locator)));
					    		try
					    		{
					    		wait.until(ExpectedConditions.elementToBeClickable(By.id(Locator)));
						    	driver.findElement(By.id(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
						    	{
						    		Thread.sleep(5000);
						    		harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	else if (LocatorType.equalsIgnoreCase("TagName"))
					    	{
					    		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(Locator)));
					    		try
					    		{
					    		wait.until(ExpectedConditions.elementToBeClickable(By.tagName(Locator)));
						    	driver.findElement(By.tagName(Locator)).click();
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Click was done Successfully");
						    	JavascriptExecutor js = (JavascriptExecutor)driver;
						    	if (js.executeScript("return document.readyState").toString().equals("complete"))
						    	{
						    		Thread.sleep(5000);
						    		harFileDownloader(driver);
							    	//RenameJsonFile.RenameFileJson(strFilePath,newUrl);
						    	}
					    		}
					    		catch(Exception e)
					    		{
					    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Unable to find the element or wrong Locator or Type");
					    			WriteLogsAndExceptions.appendToFile(e);
					    		}
					    	}
					    	
			    		}
				    	else
				    	{
				    		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The Locator or its Type is wrong or empty at the Cell ["+1+","+3+"] or ["+1+","+4+"]");
				    	}
	    		    }    
	    		    else
	    		    {
		    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The EventsName is Wrong or Empty at the Cell ["+1+","+5+"]");
		    		}
	    		}
	    		else
	    		{
	    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The Click Event is Wrong or Empty or Not-Required at the Cell ["+1+","+2+"]");
	    		}
			}
			catch (Exception e)
			{
				WriteLogsAndExceptions.appendToFile(e);
			}
		}
		public static void VideoEvent(String url) throws Exception
		{
			URL1 = url.toLowerCase();
		}
		public static void harFileDownloader(WebDriver driver) throws Exception
		{
			ReadObject objects = new ReadObject();
	    	Properties allObjects =  objects.getObjectRepository();
	    	String downloadsRenamePath = allObjects.getProperty("downloadsRenamePath");
	    	String sFileName = downloadsRenamePath + "\\test.com.har";
	    	
	    	Har har1 = proxy.getHar();
			File harFile1 = new File(sFileName);
			try 
			{
				har1.writeTo(harFile1);
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"Downloading and Renaming the Network Data Json File");
				RenameFile.methodRename();
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The File got renamed");
				JSReader.JSExecutor(driver);
			} 
			catch (Exception e) 
			{
				WriteLogsAndExceptions.appendToFile(e);
			}
		}
}
