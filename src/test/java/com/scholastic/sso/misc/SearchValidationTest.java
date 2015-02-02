package com.scholastic.sso.misc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.ErrorUtil;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.util.TestUtil;
import com.scholastic.sso.util.Xls_Reader;

public class SearchValidationTest extends TestBase{
	

  @BeforeTest()
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
  }
	
//	@Parameters("browser")
	@Test(dataProviderClass=TestDataProvider.class, dataProvider="MiscDataProvider")
	public void searchValidationTest(Hashtable<String, String> table) throws InterruptedException, IOException{
		validateRunmodes("SearchValidationTest", Constants.MISC_SUITE, table.get(Constants.RUNMODE_COL));
		APPLICATION_LOG.debug("******** Executing -  Search Validation Test ********");
//		System.out.println(b);
//		DesiredCapabilities cap = null; 
//		DesiredCapabilities cap = DesiredCapabilities.firefox();
//			WebDriver driver = new FirefoxDriver();	
		/*
		 if(b.equals("firefox")){
			cap = DesiredCapabilities.firefox();
			cap.setBrowserName("firefox");
			cap.setPlatform(Platform.ANY);
		}else if (b.equals("chrome")){
			cap = DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			cap.setPlatform(Platform.ANY);
		}
		RemoteWebDriver driver = new FirefoxDriver();
		RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
		*/
		
		Xls_Reader data = new Xls_Reader("C:\\Users\\chaitman\\Desktop\\SSO\\SSOFwk\\sso_automation\\TestData\\SSOSearchTestData.xlsx");
		int rowCount = data.getRowCount("Data");
		APPLICATION_LOG.debug("Number of Rows: " + rowCount);
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
//		TestUtil.goToStore(CONFIG.getProperty("STAGE_URL"));
		
		for(int i = 2;i<=rowCount;i++){
			String keyW = data.getCellData("Data", "Keyword", i);
//			APPLICATION_LOG.debug("Keyword from the Sheet: " + keyW);
			String cat = data.getCellData("Data", "Category", i);
			Thread.sleep(3000);
			driver.findElement(By.id("SearchField")).sendKeys(keyW);
//			driver.findElement(By.xpath("//*[@id='SearchField']")).sendKeys(keyW);
			Select sel = new Select(driver.findElement(By.xpath("//*[@id='KeywordFilter']")));
			sel.selectByVisibleText(cat);
			driver.findElement(By.xpath("//*[@id='SearchButton']")).click();
			
			String H1 = driver.findElement(By.xpath("//*[@id='CurrentSearchBox']/ul/li[1]/h1")).getText();
			System.out.println(H1);
			String H2 = null;
			WebElement noResult = null;
			
			try{
				noResult = driver.findElement(By.xpath("//*[@id='noresults']"));
				}catch(Throwable t){
//					APPLICATION_LOG.debug("No Results Found - text NOT Found");
					ErrorUtil.addVerificationFailure(t);
				}
			
			try{
			H2 = driver.findElement(By.xpath("//*[@id='CurrentSearchBox']/ul/li[2]/h2")).getText();
			System.out.println(H2);
			}catch(Throwable t){
//				APPLICATION_LOG.debug("H2 NOT Found");
				ErrorUtil.addVerificationFailure(t);
			}
			
			if(H1.equals(keyW) && H2.equals(cat) ){
				data.setCellData("Data", "Result", i, "PASS");
			}else if (H1 == null || H2 == null && noResult != null) {
				data.setCellData("Data", "Result", i, "PASS");
			}else if (H1 == null || H2 == null) {
				data.setCellData("Data", "Result", i, "FAIL");
			}else{
				data.setCellData("Data", "Result", i, "FAIL");
			}
			
			}
			
		}
	
	@AfterMethod
	public static void takeScreenShotOnFailure(ITestResult testResult)throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			System.out.println(testResult.getName());
			String screenShotFileName = CONFIG.getProperty("SCREENSHOT_DIR") + "/" + testResult.getName() + ".jpg";
			FileUtils.copyFile(scrFile, new File(screenShotFileName));
		}
	}
	
	@AfterTest
	public void shutDriver() throws InterruptedException {

		try {
			dispose();
		} catch (Throwable t) {
			return;
		}
	}
	}