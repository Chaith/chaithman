package com.scholastic.sso.misc;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.util.TestUtil;

public class StoriaSamplerEReaderTest extends TestBase{
	
	@BeforeTest()
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
  }
	
	@Test(dataProviderClass=TestDataProvider.class, dataProvider="MiscDataProvider")
	public void storiaSamplerEReaderTest(Hashtable<String, String> table) throws InterruptedException, IOException{
		APPLICATION_LOG.debug("******** Executing -  Storia Sampler eReader Test ********");
		validateRunmodes("StoriaSamplerEReaderTest", Constants.MISC_SUITE, table.get(Constants.RUNMODE_COL));

		TestUtil.goToStore(CONFIG.getProperty("STORIA_URL"));
		
		driver.findElement(By.xpath("//*[@id='myTab']/li[3]/a/span")).click();
		
		WebElement resBox = driver.findElement(By.xpath("//*[@id='kindergarten']/ul"));
//		List<WebElement> books = resBox.findElements(By.tagName("img"));
//		int booksCount = books.size();
//		System.out.println("Total number of Books - "+ booksCount);
//		Random r = new Random();
//		books.get(r.nextInt(books.size())).click();
		
		driver.findElement(By.xpath("//*[@id='kindergarten']/ul/li[1]/table/tbody/tr/td/div/img")).click();
		
		Thread.sleep(6000);
		Set<String> windows = driver.getWindowHandles();
	    Iterator<String> window = windows.iterator();
	    
	    while(window.hasNext()) {
	    	Thread.sleep(3000);
	    	driver.switchTo().window(window.next());
//	    	System.out.println(driver.getTitle());
	    	APPLICATION_LOG.debug(driver.getTitle());
	    }
	    
	    Thread.sleep(3000);
	    WebElement ReadToMe = driver.findElement(By.xpath("//*[@id='topnav']/div[3]/div[2]/div[1]"));
	    WebElement Notes = driver.findElement(By.xpath("//*[@id='topnav']/div[3]/div[3]/div[1]"));
	    WebElement Highlight = driver.findElement(By.xpath("//*[@id='topnav']/div[3]/div[4]/div[1]"));
	    WebElement GoTo = driver.findElement(By.xpath("//*[@id='botnav']/div[3]"));
	    WebElement nxtButton = driver.findElement(By.xpath("//*[@id='reader']/div[6]"));
	    
	    if (!ReadToMe.isDisplayed()){
	    	APPLICATION_LOG.debug("Read To Me - option NOT present");
	    }else if (!Notes.isDisplayed()){
	    	APPLICATION_LOG.debug("Notes - option NOT present");
	    }else if (!Highlight.isDisplayed())
	    {
	    	APPLICATION_LOG.debug("Highlight - option NOT present");
	    }else if (!GoTo.isDisplayed())
	    {
	    	APPLICATION_LOG.debug("Go To - option NOT present");
	    }
	    
	    while (nxtButton.isDisplayed()){
    	nxtButton.click();
	    	Thread.sleep(2000);
	    
    }
	    try{
	    Assert.assertEquals("A", "B");
	    }catch (Throwable t){
	    	APPLICATION_LOG.debug(t);
	    	Assert.fail();
	    }
	    
//	    Assert.assertEquals("A", "B");
	    
	    WebElement closeBtn = driver.findElement(By.xpath("//*[@id='reader']/div[14]/div"));
	    closeBtn.click();
	    
//	    driver.quit();
	}
	
	@AfterMethod
	public static void takeScreenShotOnFailure(ITestResult testResult)throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			System.out.println(testResult.getName());
			String screenShotFileName = CONFIG.getProperty("SCREENSHOT_DIR") + "/" + testResult.getName() + ".jpg";
			FileUtils.copyFile(scrFile, new File(screenShotFileName));
			driver.quit();
		}
	}
}

