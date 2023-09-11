package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import config.AppUtil;
import utilities.ExcelFileUtil;

public class DriverScript extends AppUtil{
String inputpath ="./FileInput/LoginData.xlsx";
String outputpath ="./FileOutPut/DataDrivenResults.xlsx";
ExtentReports report;
ExtentTest logger;
@Test
public void startTest() throws Throwable
{
	//define path of html
	report= new ExtentReports("./Reports/LoginTest.html");
	//create reference object
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//count no of rows in login sheet
	int rc =xl.rowCount("Login");
	Reporter.log("No of rows "+rc,true);
	//iterate all rows
	for(int i=1;i<=rc;i++)
	{
		//test case starts here
		logger = report.startTest("Login Test");
		logger.assignAuthor("Ranga");
		//read username and password cells
		String username =xl.getCellData("Login", i, 0);
		String password = xl.getCellData("Login", i, 1);
		boolean res =FunctionLibrary.adminLogin(username, password);
		if(res)
		{
			logger.log(LogStatus.PASS, "Login success");
			//if it is true write as login success into results cell
			xl.setCellData("Login", i, 2, "Login Success", outputpath);
			//write a s pass into status cell
			xl.setCellData("Login", i, 3, "Pass", outputpath);
		}
		else
		{
			
			File screen =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screen, new File("./ScreenShot/Iteration/"+i+"Loginpage.png"));
			//capture error message
			String Error_Message= driver.findElement(By.xpath(conpro.getProperty("ObjError"))).getText();
			xl.setCellData("Login", i, 2, Error_Message, outputpath);
			xl.setCellData("Login", i, 3, "Fail", outputpath);
			logger.log(LogStatus.FAIL, Error_Message);
		}
		report.endTest(logger);
		report.flush();
	}
	
}

}










