package Utility;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import TestExecute.ExecuteTest;

public class InputFileCreation 
{
	static String FrameworkInputFile = ExecuteTest.FrameworkInputFile;
	
	public static void Loadeventfileinput(String URL) throws IOException 
	{
		Workbook workbook = new XSSFWorkbook();
		Sheet Sheet = workbook.createSheet("Data");

		Object[][] bookData = {{"URL", "Load Event- Network Traffic"}, {URL, "Yes"}};
		int rowCount = 0;
		for (Object[] aBook : bookData) 
		{
			Row row = Sheet.createRow(rowCount++);
			int columnCount = 0;
			for (Object field : aBook) 
			{
				Cell cell = row.createCell(columnCount++);
				if (field instanceof String) 
				{
					cell.setCellValue((String) field);
				}
				else if (field instanceof Integer) 
				{
					cell.setCellValue((Integer) field);
				}
			}
		}

		try (FileOutputStream outputStream = new FileOutputStream(FrameworkInputFile))
		{
			workbook.write(outputStream);
		}
		workbook.close();
	}

	public static void Clickeventfileinput(String URL, String objectType, String objectvalue, String ActivityName) throws IOException 
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet Sheet = workbook.createSheet("Data");
		
		Object[][] bookData = {{"URL", "Load Event- Network Traffic", "Click Event - Network Traffic", "Locator Type", "Locator", "ActivityName"}, {URL, " ",  "Yes", objectType, objectvalue, ActivityName}};
		int rowCount = 0;
		for (Object[] aBook : bookData) 
		{
			Row row = Sheet.createRow(rowCount++);
			int columnCount = 0;
			for (Object field : aBook) 
			{
				Cell cell = row.createCell(columnCount++);
				if (field instanceof String) 
				{
					cell.setCellValue((String) field);
				} 
				else if (field instanceof Integer) 
				{
					cell.setCellValue((Integer) field);
				}
			}
		}
		try (FileOutputStream outputStream = new FileOutputStream(FrameworkInputFile)) 
		{
			workbook.write(outputStream);
		}
		workbook.close();
		}

}
