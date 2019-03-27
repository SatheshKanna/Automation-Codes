package ReadandWrite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {
	
	/*static String path = System.getProperty("user.dir");
	static String Outputfile= path + "\\Output\\TestcaseResults.xlsx";*/
	
	static String sheetName = "TestCaseResult";
	static Workbook wb = new XSSFWorkbook();
	static Sheet sheet = wb.createSheet(sheetName) ;

	public void WritinginExcel() throws IOException
	{
		ReadObject objects = new ReadObject();
    	Properties allObjects =  objects.getObjectRepository();
    	String SeleniumOutputFile = allObjects.getProperty("SeleniumOutputFile");
    	
		int rowid = 1;
        int header =0;
		Row row2 = sheet.createRow(header);
	    Cell cell = row2.createCell(0);
		cell.setCellValue("Test Case Name");	
		Cell cel2 = row2.createCell(1);
		cel2.setCellValue("Test case description");
		
		for(int i=0; i<4; i++)
		{
			Row row4 = sheet.createRow(rowid);
			Cell cell1 = row4.createCell(0);
    		cell1.setCellValue("Name " + i);
    		Cell cell2 = row4.createCell(1);
    		cell2.setCellValue("Test Pass");
    		rowid++;
		}
		FileOutputStream fileOut = new FileOutputStream(SeleniumOutputFile);
		wb.write(fileOut);
		fileOut.close();
	}
}