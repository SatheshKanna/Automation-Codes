package NewtworKTrafficReaderPackage;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import ReadandWrite.ReadObject;
import logsAndExceptionWriter.WriteLogsAndExceptions;

public class RenameFile 
{
	
	/*static String path = System.getProperty("user.dir");
	static String downloadsRenamePath = path + "\\Downloads";*/
	
	/*static String downloadsRenamePath = System.getProperty("downloadsRenamePath");*/
	
	static int m = 1;
	public static String methodRename() throws Exception
	{
		ReadObject objects = new ReadObject();
    	Properties allObjects =  objects.getObjectRepository();
    	String downloadsRenamePath = allObjects.getProperty("downloadsRenamePath");
		
		String filepathwithname="";
		try
		{
		RenameFile rf= new RenameFile();
		Thread.sleep(5000);
		
		File newfile = rf.getTheNewestFile(downloadsRenamePath, "har");
		Thread.sleep(5000);
		filepathwithname = downloadsRenamePath + "\\test" + m + ".json";
		m=m+1;
		newfile.renameTo(new File(filepathwithname));
		Thread.sleep(2000);
		String filename= newfile.getName();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The Downloaded File is : "+filename);
		RenameFile rf1 = new RenameFile();
		File updated = rf1.getTheNewestFile(downloadsRenamePath, "json");
		Thread.sleep(2000);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"The Path of the Downloaded File is : "+ updated);
		}
		catch (Exception e) 
    	{
			WriteLogsAndExceptions.appendToFile(e);
		}
		return filepathwithname;
    }
   
	public File getTheNewestFile(String filePath, String a) 
	{
		File theNewestFile = null;
		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + a);
		File[] files = dir.listFiles(fileFilter);

		if (files.length > 0) 
		{
	       /** The newest file comes first **/
	       Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
	       theNewestFile = files[0];
		}
		return theNewestFile;
	}
}
