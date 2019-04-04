package ITP;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MarkAttendance 
{
	static String mailPath = "C:\\Users\\sathesh.gunasekaran\\workspace\\ITP\\Mail";
	static WebDriver driver;
	public static void main(String[] args) throws Exception 
	{
		try
		{
	//		Parameters
			String Username = args[0];
			String Password = args[1];
			String morngOrEveng = args[2];
			String driverPath = args[3];
//			String Username = "anusha.rayankula";
//			String Password = "Smiley@dec26";
//			String morngOrEveng = "evening";
//			String driverPath = "C:\\Users\\sathesh.gunasekaran\\workspace\\ITP\\Driver\\chromedriver.exe";
			
			
			//Driver SetUp
			System.setProperty("webdriver.chrome.driver", driverPath);
			driver = new ChromeDriver();
			WebDriverWait wait = new WebDriverWait(driver, 30);
			driver.manage().window().maximize();
			
			driver.get("http://itpportal.accenture.com/ITPAttendance/LoginAction.do");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='login.do']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id("userName"))).clear();
			wait.until(ExpectedConditions.elementToBeClickable(By.id("userName"))).sendKeys(Username);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("password"))).clear();
			wait.until(ExpectedConditions.elementToBeClickable(By.id("password"))).sendKeys(Password);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("loginButton"))).click();
			Thread.sleep(10000);
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='#attendance']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Mark Attendance']"))).click();
			
			if(morngOrEveng.equalsIgnoreCase("morning"))
			{
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='checkbox']"))).click();
				driver.switchTo().alert().accept();
				System.out.println("Marked the Morning Attendance");
				sendEmailNoSMTP.runBAT("sendEmailMorning.bat", mailPath);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='logout.do']"))).click();
			}
			
			else if(morngOrEveng.equalsIgnoreCase("evening"))
			{
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='checkbox']"))).click();
				driver.switchTo().alert().accept();
				System.out.println("Marked the Evening Attendance");
				sendEmailNoSMTP.runBAT("sendEmailEvening.bat", mailPath);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='logout.do']"))).click();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sendEmailNoSMTP.runBAT("sendEmailException.bat", mailPath);
		}
		finally
		{
			driver.quit();
		}
	}

}
