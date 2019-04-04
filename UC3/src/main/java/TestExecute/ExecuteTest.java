package TestExecute;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

import NewtworKTrafficReaderPackage.Backupfilecreator;
import ReadandWrite.ReadExcel;
import ReadandWrite.ReadObject;
import ReadandWrite.UIOperation;
import logsAndExceptionWriter.FileZipperForLogsandException;
import logsAndExceptionWriter.WriteLogsAndExceptions;
import net.lightbody.bmp.BrowserMobProxy;

public class ExecuteTest 
{
    static WebDriver webdriver;
    public static String ConfigFilePath;
    public static String FrameworkInputFile;
    String URL;
    BrowserMobProxy proxy;
    
    /*String downloadsRenamePath = System.getProperty("downloadsRenamePath");
    String ExceptionsPath = System.getProperty("ExceptionsPath");
    String strOutputfile = System.getProperty("Outputfile");
    String LogsPath = System.getProperty("LogsPath");
    String use3inputFile = System.getProperty("inputFile");*/
    
    /*String downloadsRenamePath = path + "\\Downloads";    
    String ExceptionsFile = path + "\\Exceptions\\Exceptions.txt";
    String Outputfile = path + "\\Output\\TestcaseResults.xlsx";    
    String use3inputFile = path + "\\TestCase2.xlsx";
    String LogsFile = path + "\\Logs\\Logs.txt";*/
    
    public static void main(String [] args) throws Exception 
    {
    	try 
        {
		String use3inputFile = args[0];
    	String configFile = args[1];
//		String use3inputFile = "C:\\Users\\sathesh.gunasekaran\\Desktop\\WAITA\\Direct Call and Mozilla\\Filter_Direct.xlsx";
//    	String configFile = "C:\\WebAnalytics\\tool\\UseCase3\\UC3Objects\\object.properties";
    	ConfigFilePath = configFile;
    
		ReadObject objects = new ReadObject();
    	Properties allObjects =  objects.getObjectRepository();
    	//String use3inputFile = allObjects.getProperty("use3inputFile");
    	String downloadsRenamePath = allObjects.getProperty("downloadsRenamePath");
        String SeleniumOutputFile = allObjects.getProperty("SeleniumOutputFile");
        String LogsPath = allObjects.getProperty("LogsPath");
        String ExceptionsPath = allObjects.getProperty("ExceptionsPath");
        FrameworkInputFile = allObjects.getProperty("FrameworkInputFile");
        String LogsFile = LogsPath + "\\Logs.txt";
        String ExceptionsFile = ExceptionsPath + "\\Exceptions.txt";
        String ZiplogsFile = LogsPath + "\\Logs" + new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(new Date()) + ".zip";
        String ZippedLogs = LogsPath + "\\Backup Logs";
    	String ZipExceptionsFile = ExceptionsPath + "\\Exceptions" + new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(new Date()) + ".zip";
        String ZippedExceptions = ExceptionsPath + "\\Backup Exceptions";
        String LogsFolder = "In Logs Folder";
        String ExceptionsFolder = "In Exceptions Folder";
        int maxDays = 5;
            
		FileZipperForLogsandException.mainFileZipper(LogsFolder,LogsPath,ZiplogsFile,ZippedLogs,maxDays);
		FileZipperForLogsandException.mainFileZipper(ExceptionsFolder,ExceptionsPath,ZipExceptionsFile,ZippedExceptions,maxDays);
		WriteLogsAndExceptions.writeLogs(LogsFile);
    	FileUtils.cleanDirectory(new File(downloadsRenamePath));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())+"Cleaning all the files from the JSON Downloads");
        Backupfilecreator.CreateFileRenameExisting();
        ReadExcel file = new ReadExcel();
        Sheet Sheet1 = file.readExcel(use3inputFile);

        UIOperation operation = new UIOperation(webdriver);

        File Exceptionfile = new File(ExceptionsFile);
        long lengthinbytes = Exceptionfile.length();

        int rowCount = Sheet1.getLastRowNum();

        int rowid = 1;
        int header = 0;
        //String Objectvalue = null, objecttype = null;
        //Create OutPut Excel for Test Cases
        Workbook wb = new XSSFWorkbook();
        Sheet WriteSheet = wb.createSheet("Test Results");
        Row row2 = WriteSheet.createRow(header);
        Cell cell = row2.createCell(0);
        cell.setCellValue("Test Case Name");
        Cell cel2 = row2.createCell(1);
        cel2.setCellValue("Test case description");

        for (int i = 1; i < rowCount + 1; i++) 
        {
            Row row = Sheet1.getRow(i);
            String Teststatus = row.getCell(6).toString();
            String Testcasename = row.getCell(0).toString();
            if (Teststatus.equalsIgnoreCase("Yes")) 
            {
                Row row4 = WriteSheet.createRow(rowid);
                try 
                {
                    for (int j = i + 1; j <= rowCount; j++) 
                    {
                        Row row1 = Sheet1.getRow(j);
                        //boolean testing = row1.getCell(0).getStringCellValue().isEmpty();
                        if (row1.getCell(0)==null ||  row1.getCell(0).getStringCellValue().isEmpty()) 
                        {
                        	String soperation;
                        	String objectType;
                        	String objectvalue;
                        	String value;
                        	String ActivityName;
                        	if(row1.getCell(1)==null)
                        	{
                        		soperation="";
                        	}
                        	else
                        	{
                        		soperation=row1.getCell(1).getStringCellValue();
                        	}
                        	if(row1.getCell(2)==null) {
                        		objectType="";
                        	}
                        	else
                        	{
                        		objectType=row1.getCell(2).getStringCellValue();
                        	}
                        	if(row1.getCell(3)==null) {                        		
                        		objectvalue="";
                        	}
                        	else
                        	{
                        		objectvalue=row1.getCell(3).getStringCellValue();
                        	}
                        	if(row1.getCell(4)==null) {
                        		value="";
                        	}
                        	else
                        	{
                        		value=row1.getCell(4).getStringCellValue();
                        	}
                        	if(row1.getCell(5)==null) 
                        	{
                        		ActivityName="";
                        	}
                        	else
                        	{
                        		ActivityName=row1.getCell(5).getStringCellValue();
                        	}
                             operation.perform(soperation, objectType, objectvalue, value, ActivityName);
                            i++;
                        } 
                        else 
                        {
                            break;
                        }
                    }
                    if (Exceptionfile.exists()) 
                    {
                        long lengthafterexecution = Exceptionfile.length();
                        if (lengthafterexecution > lengthinbytes) 
                        {
                            lengthinbytes = lengthafterexecution;
                            Cell cell1 = row4.createCell(0);
                            cell1.setCellValue(Testcasename);
                            Cell cell2 = row4.createCell(1);
                            cell2.setCellValue("Test Fail");
                        } 
                        else 
                        {
                            Cell cell1 = row4.createCell(0);
                            cell1.setCellValue(Testcasename);
                            Cell cell2 = row4.createCell(1);
                            cell2.setCellValue("Test Pass");
                        }
                    }
                    else
                    {
                    	Cell cell1 = row4.createCell(0);
                        cell1.setCellValue(Testcasename);
                        Cell cell2 = row4.createCell(1);
                        cell2.setCellValue("Test Pass");
                    }
                } 
                catch (Exception e) 
                {
                	WriteLogsAndExceptions.appendToFile(e);
                }
                rowid++;
            } 
            else if (Teststatus.equalsIgnoreCase("NO")) 
            {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())+"Test case " + row.getCell(0).toString() + " not executed");
                i++;
                for (int j = i; j <= rowCount; j++) 
                {
                    Row row1 = Sheet1.getRow(j);
                    boolean testing = row1.getCell(1) == null;
                    if (!testing) 
                    {
                        i++;
                    } 
                    else 
                    {
                        i--;
                        break;
                    }
                }
            } 
            else 
            {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())+"The test case execution is neithor Yes nor No in the UseCase3 input excel");
            }
        }
        //check directory Exist if Not Create.
        File filePath=new File(SeleniumOutputFile);
		File directory=new File(filePath.getParent());
		if(!directory.isDirectory())
		{
			directory.mkdirs();
		}
        FileOutputStream fileOut = new FileOutputStream(SeleniumOutputFile);
        wb.write(fileOut);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())+ "The Selenium Output file is: " + SeleniumOutputFile);
        fileOut.close();
        wb.close();
        
        }
        catch(Exception e)
        {
        	WriteLogsAndExceptions.appendToFile(e);
        	try
        	{
        		webdriver.quit();
        	}
        	catch(Exception ex){}
        }
        finally 
        {
        	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())+ "The Selenium Process ended");
        }
    }
}