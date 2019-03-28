package logsAndExceptionWriter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class FileZipperForLogsandException 
{
	static List<String> fileList = new ArrayList<String>();
   
    public static void mainFileZipper(String ExecutionIn, String Path, String ZipFile,String ZippedLogsExceptions,int maxDays) throws Exception
    {
    	fileList.clear();
    	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + ExecutionIn);
    	
    	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Generating the list of files in the folder");
    	FileZipperForLogsandException.generateFileList(new File(Path),Path);
    	
    	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Zipping all the older files in the folder");
    	FileZipperForLogsandException.zipIt(ZipFile,Path);
    	
    	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Deleting all the already zipped files from the folder");
    	FileZipperForLogsandException.deleteZippedFiles(Path);
    	
    	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Moving all the list of zipped files to another sub-folder");
    	FileZipperForLogsandException.moveZippedFiles(Path,ZippedLogsExceptions);
    	
    	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Deleting all the files in the Sub-Folder which are older than 30 days");
    	FileZipperForLogsandException.deleteFilesOlderthanxdays(maxDays, ".zip", ZippedLogsExceptions);
    	
    }
    public static void zipIt(String ZipFile, String Path) throws Exception
    {
	     byte[] buffer = new byte[1024];
	     try
	     {
	    	FileOutputStream fos = new FileOutputStream(ZipFile);
	    	ZipOutputStream zos = new ZipOutputStream(fos);
	    	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The path of the Zip File is: " + ZipFile);
	    	for(String file : fileList)
	    	{
	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze);
	        	FileInputStream in = new FileInputStream(Path + File.separator + file);
	        	int len;
	        	while ((len = in.read(buffer)) > 0) 
	        	{
	        		zos.write(buffer, 0, len);
	        	}
	        	in.close();
	        	//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The added file to Zip file is: " + file);
	    	}
	    	zos.closeEntry();
	    	zos.close();
	     }
		 catch(Exception e)
		 {
			 e.printStackTrace();
			 //exceptionWriter.appendToFile(e);  
		 }
    }
    public static void generateFileList(File node, String Path) throws Exception
    {
    	try
    	{
	    	//add file only
			if(node.isFile() && node.getPath().contains(".txt"))
			{
				fileList.add(generateZipEntry(node.getAbsoluteFile().toString(),Path));
			}
			if(node.isDirectory())
			{
				String[] subNote = node.list();
				for(String filename : subNote)
				{
					generateFileList(new File(node, filename),Path);
				}
			}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		//exceptionWriter.appendToFile(e);
    	}
    }
    
    private static String generateZipEntry(String file, String Path)
    {
    	return file.substring(Path.length()+1, file.length());
    }
    
    public static void deleteZippedFiles(String folder) throws Exception
    {
    	try
    	{
	    	File dir = new File(folder);
			FileFilter fileFilter = new WildcardFileFilter("*." + "txt");
			File[] files = dir.listFiles(fileFilter);
			for (File file : files)
		    {
				if (!file.isDirectory())
		        {
					file.delete();
		        }
				
			}
			//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Deleted all the files which are zipped");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		//exceptionWriter.appendToFile(e);
    	}
    }
    
    public static void moveZippedFiles(String folder, String moveFolder) throws Exception
    {
    	try
    	{
    		File directory = new File(moveFolder);
    		if (! directory.exists())
    		{
    	        directory.mkdir();
    	    }
    		File dir = new File(folder);
    		File moveDir = new File(moveFolder);
    		FileFilter fileFilter = new WildcardFileFilter("*." + "zip");
    		File[] files = dir.listFiles(fileFilter);
    		for (File file : files)
    	    {
    			if (!file.isDirectory())
    	        {
    				file.renameTo(new File(moveDir, file.getName()));
    	        }
    			
    		}
    		//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Moved all the files which are Zipped");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		//exceptionWriter.appendToFile(e);
    	}
    }
    
    public static void deleteFilesOlderthanxdays(long days, String fileExtension, String ZippedLogsExceptions) throws Exception
    {
    	try
    	{
    		File folder = new File(ZippedLogsExceptions);
            if (folder.exists()) 
            {
                File[] listFiles = folder.listFiles();
                long eligibleForDeletion = System.currentTimeMillis() -(days * 24 * 60 * 60 * 1000);
                for (File listFile: listFiles) 
                {
                    if (listFile.getName().endsWith(fileExtension) &&
                        listFile.lastModified() < eligibleForDeletion) 
                    {
                        if (!listFile.delete()) 
                        {
                            //System.out.println("Sorry Unable to Delete the file:" + listFile);
                        }
                    }
                }
            }
            //System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "Deleted all the files which are older than 30 days from the Sub-Folder");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		//exceptionWriter.appendToFile(e);
    	}
    }
}
