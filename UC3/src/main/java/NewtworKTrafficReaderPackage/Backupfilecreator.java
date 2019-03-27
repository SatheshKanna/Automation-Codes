package NewtworKTrafficReaderPackage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import ReadandWrite.ReadObject;
import logsAndExceptionWriter.WriteLogsAndExceptions;


public class Backupfilecreator 
{
	/*static String path = System.getProperty("user.dir");
	static String MasterInputFile =  path + "\\Master.xlsx";
	static String InputForPythonFile = path + "\\ComparingExcel.xlsx";
	
	static String MasterInputFile = System.getProperty("MasterInputFile");
	static String InputForPythonFile = System.getProperty("InputForPythonFile");*/

    public static void CreateFileRenameExisting() throws Exception
    {
		ReadObject objects = new ReadObject();
    	Properties allObjects =  objects.getObjectRepository();
    	String MasterInputFile = allObjects.getProperty("MasterInputFile");
    	String InputForPythonFile = allObjects.getProperty("InputForPythonFile");
		try
		{
			File file = new File(InputForPythonFile);
			file.delete();
		}
		catch(Exception e)
		{
			WriteLogsAndExceptions.appendToFile(e);
		}
		File from = new File(MasterInputFile);
        File to = new File(InputForPythonFile);
        FileUtils.copyFile(from, to);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"A BackUp Input File for Python has been created for executing JavaScript on respective URL's");
    }
}
