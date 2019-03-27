package ReadandWrite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utility.InputFileCreation;
import logsAndExceptionWriter.WriteLogsAndExceptions;
import net.lightbody.bmp.proxy.CaptureType;
import NewtworKTrafficReaderPackage.BrokenLinks;
import NewtworKTrafficReaderPackage.NetworkTrafficReaderClass;

public class UIOperation {
    WebDriver driver;
    Actions builder;
    JavascriptExecutor jse;
    WebDriverWait wait;
    static String url;
    static String URL;
    static String MainWindow;
    String ActivityName;
    
    
    public UIOperation(WebDriver driver) 
    {
        this.driver = driver;
    }
    public void perform(String operation, String objectType, String objectvalue, String value, String ActivityName) throws Exception 
    {
    	
		try
		{
	    	ReadObject objects = new ReadObject();
	    	Properties allObjects =  objects.getObjectRepository();
	    	String ChromeDriverPath = allObjects.getProperty("ChromeDriverPath");
	    	String FirefoxDriverPath = allObjects.getProperty("FirefoxDriverPath");
	    	String ScreenShotPath = allObjects.getProperty("ScreenShotPath");
			
	        switch (operation.toUpperCase()) 
	        {       
	            case "CHROMEBROWSER":
	                try 
	                {
	                    NetworkTrafficReaderClass.proxySetUp();
	                    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	            		capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
	                    capabilities.setCapability(CapabilityType.PROXY, NetworkTrafficReaderClass.seleniumProxy);
	                    if(value.equalsIgnoreCase("Incognito"))
	                    {
	                    	ChromeOptions options = new ChromeOptions();
	                        options.addArguments("--incognito");
	                        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	                    }
	                    System.setProperty("webdriver.chrome.driver", ChromeDriverPath);
	                    driver = new ChromeDriver(capabilities);
	                    builder = new Actions(driver);
	                    jse = (JavascriptExecutor) driver;
	                    wait= new WebDriverWait(driver, 30);
	                    MainWindow = driver.getWindowHandle();
	                } 
	                catch (Exception e)
	                {
	                	WriteLogsAndExceptions.appendToFile(e);
	                }
	                break;
	            case "FIREFOXBROWSER":
	                try 
	                {
	                    NetworkTrafficReaderClass.proxySetUp();
	                    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	                    capabilities.setJavascriptEnabled(true);
	                    capabilities.setCapability(CapabilityType.PROXY, NetworkTrafficReaderClass.seleniumProxy);
	            	    //capabilities.setCapability("marionette",true);
	            	    capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
	            	    //capabilities.setCapability(CapabilityType.SUPPORTS_NETWORK_CONNECTION, true);
	            	    System.setProperty("webdriver.gecko.driver", FirefoxDriverPath);
	            		driver = new FirefoxDriver(capabilities);
	            		builder = new Actions(driver);
	                    jse = (JavascriptExecutor) driver;
	                    wait= new WebDriverWait(driver, 30);
	            		MainWindow = driver.getWindowHandle();
	                } 
	                catch (Exception e)
	                {
	                	WriteLogsAndExceptions.appendToFile(e);
	                }
	                break;
	
	            case "CLICK":
	            	wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(objectvalue,objectType)));
	    			wait.until(ExpectedConditions.elementToBeClickable(this.getObject(objectvalue,objectType)));
	                driver.findElement(this.getObject(objectvalue, objectType)).click();
	                break;
	            case "INPUT":
	            	wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(objectvalue,objectType)));
	    			wait.until(ExpectedConditions.elementToBeClickable(this.getObject(objectvalue,objectType)));
	                driver.findElement(this.getObject(objectvalue, objectType)).sendKeys(value);
	                break;
	            case "SELECT":
	            	wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(objectvalue,objectType)));
	    			wait.until(ExpectedConditions.elementToBeClickable(this.getObject(objectvalue,objectType)));
	                Select dropdown = new Select(driver.findElement(this.getObject(objectvalue, objectType)));
	                dropdown.selectByValue(value);
	                driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
	                break;
	            case "NAVIGATE":
	            	NetworkTrafficReaderClass.proxy.disableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
	                URL = value;
	                BrokenLinks.isLinkBroken(URL);
			    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The Response Code of the Url is: " + BrokenLinks.respCode);
			    	if(BrokenLinks.respCode==200 || BrokenLinks.respCode==401)
			    	{
		                driver.switchTo().window(MainWindow);
						NetworkTrafficReaderClass.proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
		                NetworkTrafficReaderClass.proxy.newHar(URL);
		                //driver.get(URL);
		                driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
		                driver.navigate().to(URL);
			    	}
			    	else
			    	{
			    		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + "The URL provided is an Broken Link");
			    	}
	                break;
	            case "GETTHEOPENEDTAB":
	            	for(String winHandle : driver.getWindowHandles())
	            	{
	            	    driver.switchTo().window(winHandle);
	            	}
	            	break;
	            case "SWITCHTOPARENTTAB":
	            	driver.switchTo().window(MainWindow);
	            	break;
	            case "CLOSETAB":
	            	driver.close();
	            	if(!driver.toString().contains("(null)"))
	            	{
	            		driver.switchTo().window(MainWindow);
	            	}
	            	break;
	            case "GETTEXT":
	            	wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(objectvalue,objectType)));
	    			wait.until(ExpectedConditions.elementToBeClickable(this.getObject(objectvalue,objectType)));
	                driver.findElement(this.getObject(objectvalue, objectType)).getText();
	                break;
	            case "MAXIMIZE":
	                driver.manage().window().maximize();
	                break;
	            case "CLOSEBROWSER":
	                driver.quit();
	                NetworkTrafficReaderClass.proxy.stop();
	                break;
	            case "EXECUTEJAVASCRIPT":
	            	jse.executeScript(value);
	                break;
	            case "EXECUTEJAVASCRIPTONELEMENT":
	            	jse.executeScript(value,driver.findElement(this.getObject(objectvalue, objectType)));
	            	break;
	            case "STARTCAPTURINGEVENT":
	            	NetworkTrafficReaderClass.proxy.disableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
	            	NetworkTrafficReaderClass.proxy.newHar(URL+"|"+ActivityName);
	            	NetworkTrafficReaderClass.proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
	                break;
	            case "GETNETWORKEVENT":
	            	NetworkTrafficReaderClass.VideoEvent(URL+"|"+ActivityName);
	            	NetworkTrafficReaderClass.harFileDownloader(driver);
	            	break;
	            case "MOVETOELEMENT":
	            	builder.moveToElement(driver.findElement(this.getObject(objectvalue, objectType))).build().perform();
	                break;
	            case "MOVETOELEMENTANDCLICK":
	            	builder.moveToElement(driver.findElement(this.getObject(objectvalue, objectType))).click().build().perform();
	                break;
	            case "SCROLLDOWN":
	                jse.executeScript("scrollBy(0,250)");
	                break;
	            case "SCREENSHOT":
	                {
	                    TakesScreenshot scrShot = ((TakesScreenshot) driver);
	                    File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
	                    File DestFile = new File(ScreenShotPath + value + new SimpleDateFormat("yyyy-MM-dd HHmmss ").format(new Date()) + ".png");
	                    FileUtils.copyFile(SrcFile, DestFile);
	                }
                    break;
	            case "LOADEVNTNETWORKACTIVITY":
	                {
	                    InputFileCreation.Loadeventfileinput(URL);
	                    try 
	                    {
	                        NetworkTrafficReaderClass.inputExcelReader();
	                        NetworkTrafficReaderClass.UrlandLoadEventCheck(driver);
	                        Thread.sleep(5000);
	                    } 
	                    catch (Exception e) 
	                    {
	                    	WriteLogsAndExceptions.appendToFile(e);
	                    }
	                    break;
	                }
	            case "CLICKEVNTNETWORKACTIVITY":
	                {
	                	NetworkTrafficReaderClass.proxy.disableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
	                	NetworkTrafficReaderClass.proxy.newHar(URL+"|"+ActivityName);
	                	NetworkTrafficReaderClass.proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
	                    InputFileCreation.Clickeventfileinput(URL, objectType, objectvalue, ActivityName);
	                    try 
	                    {
	                        NetworkTrafficReaderClass.inputExcelReader();
	                        NetworkTrafficReaderClass.UrlandLoadEventCheck(driver);
	                        NetworkTrafficReaderClass.ClickEventCheck(driver);
	                        Thread.sleep(5000);
	                    } catch (Exception e) 
	                    {
	                    	WriteLogsAndExceptions.appendToFile(e);
	                    }
	                    break;
	                }
	            case "WAIT":
	                {
	                    Thread.sleep(Integer.valueOf(value));
	                    break;
	                }
	            case "WAITFORVIDEOPAUSECOOKIE":
	            	Cookie ck = driver.manage().getCookieNamed(ActivityName);
	        		String Cook = ck.toString();
	        		String Checker = Cook.split(";")[0];
	        		while(Cook.contains(Checker))
	        		{
	        			Thread.sleep(Integer.valueOf(value));
	        			ck = driver.manage().getCookieNamed(ActivityName);
	        			Cook = ck.toString();
	        		}
	        		Thread.sleep(2000);
	        		break;
	            default:
	                	break;
	        }
		}
		catch (Exception e) 
	    {
	    	WriteLogsAndExceptions.appendToFile(e);
	    	driver.quit();
	    }
    }
    private By getObject(String objectvalue, String objectType) throws Exception {
        if (objectType.equalsIgnoreCase("XPATH")) {

            return By.xpath(objectvalue);
        } else if (objectType.equalsIgnoreCase("CLASSNAME")) {

            return By.className(objectvalue);

        } else if (objectType.equalsIgnoreCase("ID")) {

            return By.id(objectvalue);

        } else if (objectType.equalsIgnoreCase("NAME")) {

            return By.name(objectvalue);

        } else if (objectType.equalsIgnoreCase("CSS")) {

            return By.cssSelector(objectvalue);

        } else if (objectType.equalsIgnoreCase("LINK")) {

            return By.linkText(objectvalue);

        } else if (objectType.equalsIgnoreCase("PARTIALLINK")) {

            return By.partialLinkText(objectvalue);

        } else {
            throw new Exception("Wrong object type");
        }
    }
}