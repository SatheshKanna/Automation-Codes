package JavascriptConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ReadandWrite.ReadObject;
import logsAndExceptionWriter.WriteLogsAndExceptions;


public class WriteExcel 
{
	
	/*static String InputForPythonFile = System.getProperty("InputForPythonFile");
	
	static String path = System.getProperty("user.dir");
	static String InputForPythonFile = path + "\\ComparingExcel.xlsx";*/
	
    public static void writeExcel() throws Exception
    {
    	try
    	{
			ReadObject objects = new ReadObject();
	    	Properties allObjects =  objects.getObjectRepository();
	    	String InputForPythonFile = allObjects.getProperty("InputForPythonFile");
	        File file =    new File(InputForPythonFile);
	        FileInputStream inputStream = new FileInputStream(file);
	        Workbook wb = new XSSFWorkbook(inputStream);
	 		Sheet sh1= wb.getSheetAt(0);
		    Row row = sh1.getRow(JSReader.i);
	        Cell cell = row.getCell(JSReader.j);
	        cell.setCellValue(JSReader.dataToWrite);
		    inputStream.close();
		    FileOutputStream outputStream = new FileOutputStream(file);
		    wb.write(outputStream);
		    outputStream.close();
		    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The Master Data - Input for Python Excel got updated successfully with the Executed JavaScript Value");
		    wb.close();
    	}
    	catch(Exception e)
    	{
    		WriteLogsAndExceptions.appendToFile(e);
    	}
    	
    }
}
