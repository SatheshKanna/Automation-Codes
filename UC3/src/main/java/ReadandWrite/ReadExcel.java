package ReadandWrite;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ReadExcel 
{
    public Sheet readExcel(String filePath) throws IOException
    {
	    File file =    new File(filePath);
	    FileInputStream inputStream = new FileInputStream(file);
	    String fileExtensionName = FilenameUtils.getExtension(filePath);
	    Sheet Sheet=null;
	    Workbook Workbook = null;
	    //String fileExtensionName = fileName.substring(fileName.indexOf("."));
	    if(fileExtensionName.equals("xlsx"))
	    {
	    	Workbook = new XSSFWorkbook(inputStream);
	    	Sheet = Workbook.getSheetAt(0);
	    }
	    else if(fileExtensionName.equals("xls"))
	    {
	      Workbook = new HSSFWorkbook(inputStream);
	      Sheet = Workbook.getSheetAt(0);
	    }    
	     return Sheet;   
    }
}