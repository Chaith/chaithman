package com.scholastic.sso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.SkipException;
//import org.testng.log4testng.Logger;






import bsh.commands.dir;

import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestUtil;
import com.scholastic.sso.util.Xls_Reader;

public class TestBase {
//	public static Properties prop;
	public static Properties CONFIG=null;
	public static Properties OR=null;
	public static WebDriver dr=null;
	public static EventFiringWebDriver driver=null;
	public static Logger APPLICATION_LOG = Logger.getLogger("devpinoyLogger");
	public static String resultsFolder;
	
	
	public static void initialize() throws IOException{
		if(driver == null){
			//config property file
			CONFIG = new Properties();
			FileInputStream fn = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//project.properties");
			CONFIG.load(fn);
			
			OR = new Properties();
			fn = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//or.properties");
			OR.load(fn);
			
			//Initializing Web Driver and EventFiringWebDriver
			if(CONFIG.getProperty("browser").equals("Firefox")){
			dr = new FirefoxDriver();
			}else if (CONFIG.getProperty("browser").equals("IE")){
			dr = new InternetExplorerDriver();
			}
		driver = new EventFiringWebDriver(dr);
		driver.manage().timeouts().implicitlyWait(9, TimeUnit.SECONDS);
		}
	}	
	
/*	public static void init() throws IOException{
		if(prop == null){
			String path = System.getProperty("user.dir")+"\\src\\test\\resources\\project.properties";
			
			prop = new Properties();
			try {
				FileInputStream fs = new FileInputStream(path);
				prop.load(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}*/
	
	public void validateRunmodes(String testName, String suiteName, String dataRunmode) throws IOException{
		APPLICATION_LOG.debug("Validating runmode for "+testName+" in suite "+suiteName);
		//Suite run mode
		boolean suiteRunmode = TestUtil.isSuiteRunnable(suiteName, new Xls_Reader(CONFIG.getProperty("xlsFileLocation")+"Suite.xlsx"));
		boolean testRunmode = TestUtil.isTestCaseRunnable(testName, new Xls_Reader(CONFIG.getProperty("xlsFileLocation")+suiteName+".xlsx"));
		boolean dataSetRunmode = false;
		if(dataRunmode.equals(Constants.RUNMODE_YES))
			dataSetRunmode = true;
		
		if(!(suiteRunmode && testRunmode && dataSetRunmode)){
			APPLICATION_LOG.debug("Skipping the test - " + testName + " - inside the suite - " + suiteName);
			throw new SkipException("Skipping the test - " + testName + " - inside the suite - " + suiteName);
		}
	}
	

	
	public static WebElement getObject(String xpathKey){
		try{
		return driver.findElement(By.xpath(OR.getProperty(xpathKey)));
		}catch(Throwable t){
			return null;
		}
	}
	public static WebElement getObject(WebElement element, String xpathKey){
		try{
			return element.findElement(By.xpath(xpathKey));
		}catch(Throwable t){
			return null;
		}
	}
	
	public static WebElement getObjectByString(String xpathKey){
		try{
			return driver.findElement(By.xpath(xpathKey));
		}catch(Throwable t){
			return null;
		}
	}
	
	public static WebElement getObjectByCSS(WebElement element, String cssSelector){
		try{
			return element.findElement(By.cssSelector(cssSelector));
		}catch(Throwable t){
			return null;
		}
	}
	
	public static WebElement getObjectByCSS(String cssSelector){
		try{
			return driver.findElement(By.cssSelector(cssSelector));
		}catch(Throwable t){
			return null;
		}
	}
	
	public void dispose() throws InterruptedException{
		Thread.sleep(1000); 
		driver.close();
		Thread.sleep(1000); 
//		driver.quit();
		driver = null;
	}

	
}
