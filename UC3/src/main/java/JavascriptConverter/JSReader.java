package JavascriptConverter;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import NewtworKTrafficReaderPackage.BrokenLinks;
import NewtworKTrafficReaderPackage.NetworkTrafficReaderClass;
import ReadandWrite.ReadObject;
import logsAndExceptionWriter.WriteLogsAndExceptions;

public class JSReader 
{
	
	/*static String MasterInputFile = System.getProperty("MasterInputFile");
	
	//static String path = System.getProperty("user.dir");
	//static String MasterInputFile =  path + "\\Master.xlsx";
*/	
	static String dataToWrite;
	static int cellCount;
	static int j;
	static int i;
	static int k;
	public static void JSExecutor(WebDriver driver) throws Exception
	{
		ReadObject objects = new ReadObject();
    	Properties allObjects =  objects.getObjectRepository();
    	String MasterInputFile = allObjects.getProperty("MasterInputFile");
		try
		{
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The JavaScript in the Master Data Excel for this particular event is going to get executed");
			//System.out.println(MasterInputFile);
			File src=new File(MasterInputFile);
			FileInputStream fis=new FileInputStream(src);
	 		Workbook wb = new XSSFWorkbook(fis);
	 		Sheet sh1= wb.getSheetAt(0);
	 		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"Reading the Master Data Excel");
	 		int rowCount = sh1.getLastRowNum()-sh1.getFirstRowNum();
	 		j=1;
	 		cellCount=2;
	 		//Looping all the Master Excels URLS in the Header
	 		while (j<cellCount)
	 		{
		 		for (i = 0; i <= rowCount; i++)
				{
				    Row row = sh1.getRow(i);
				    cellCount = row.getLastCellNum();
			    	if(i==0)
			    	{
			    		if(row.getCell(j)!=null && !row.getCell(j).getStringCellValue().isEmpty() && row.getCell(j).getStringCellValue()!="")
			    		{
			    			
					    	String Url = row.getCell(j).getStringCellValue();
					    	if (NetworkTrafficReaderClass.URL1.equals(Url.toLowerCase()))
					    	{
					    		try
					    		{
					    		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The current URL " + NetworkTrafficReaderClass.URL1 + " is present in the Master File");
						    	if(Url.contains("|")) 
						    	{
						    		Url= (Url.split("\\|"))[0];
						    		
						    	}
						    	BrokenLinks.isLinkBroken(Url);
						    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Response Code of the Url is: " + BrokenLinks.respCode);
						    	if(BrokenLinks.respCode==200 || BrokenLinks.respCode==401)
						    	{
							    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Url given at the Cell ["+i+","+j+"] is a Valid Link with Response Code : " +String.valueOf(BrokenLinks.respCode));
							    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Working on URL : " + Url);
						    		/*JavascriptExecutor js = (JavascriptExecutor)driver;
						    		if (js.executeScript("return document.readyState").toString().equals("complete"))
							    	{
						    			Thread.sleep(5000);
						    			driver.manage().window().maximize();
							    	}*/
						    	}
						    	else
						    	{
						    		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Given Url at the Cell ["+i+","+j+"] is a Broken Link as its Response Code is : "+String.valueOf(BrokenLinks.respCode));
						    		break;
						    	}
					    		}
						    	catch(Exception e)
						    	{
						    		WriteLogsAndExceptions.appendToFile(e);
						    	}
					    	}
					    	else	
					    	{
					    		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Url at ["+i+","+j+"] need not be checked now as it is not same as the current Url");
					    		break;
					    	}
			    		}
				    	else
				    	{
				    		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Given Url at the Cell ["+i+","+j+"] is Empty or Blank");
				    		break;
				    	}
			    	}
			    	else if(i!=0)
			    	{
			    		k = i;
			    		if(row.getCell(j)!=null && !row.getCell(j).getStringCellValue().isEmpty() && row.getCell(j).getStringCellValue()!="")
			    		{
			    			String CellValue = row.getCell(j).getStringCellValue();
			    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +k + ". The Cell ["+i+","+j+"] value is : " + CellValue);
			    			try
			    			{
			    					JavascriptExecutor js = (JavascriptExecutor)driver;
			    				    dataToWrite= js.executeScript(CellValue).toString();
			    				    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The above mentioned cellValue is a JavaScript and the Executed JS value for this cellValue is : " + dataToWrite);
			    				    WriteExcel.writeExcel();
			    				    
			    			}
			    			catch(Exception e) 
			    			{
			    				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The above mentioned cellValue is not a JavaScript, it might be a Hard Quoted Value or REGEX or Wrong JavaScript Syntax");
			    				//exceptionWriter.appendToFile(e);
			    			}
			    			
			    		}
			    		else
			    		{
				    		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +k + ". The Cell ["+i+","+j+"] is Empty or Blank");
			    		}
			    	}
	    		}
		 		j++;
	 		}
	 		wb.close();
 		}
		catch(Exception e) 
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
	}
}
