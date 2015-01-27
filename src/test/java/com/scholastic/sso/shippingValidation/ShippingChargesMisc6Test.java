package com.scholastic.sso.shippingValidation;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.util.TestUtil;

public class ShippingChargesMisc6Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void shippingChargesMisc6Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing -  Shipping Charges Misc6 Test ********");
		validateRunmodes("ShippingChargesMisc6Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		TestUtil.selectBooks(2, true);
		getObject("CHECKOUT_NOW").click();
		
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
//		TestUtil.LoginTest();
		TestUtil.LoginTestSce1();
		TestUtil.selectAddress("AK");
		
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double shippingCharges = TestUtil.getShippingCharges();
		System.out.println(shippingCharges);
		double calcShippingCharges = 0.00;
		System.out.println(calcShippingCharges);
		
		if (shippingCharges == calcShippingCharges) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for Standard_Shipping");
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges + " and Calculated "+ calcShippingCharges);
		
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("Two_Day_Shipping")).click();
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double shippingCharges1 = TestUtil.getShippingCharges();
		System.out.println(shippingCharges1);
		double calcShippingCharges1 = TestUtil.calcTwoDayShippingCharges("AK", false, 1, 2, 0);
		System.out.println(calcShippingCharges1);
		
		if (shippingCharges1 == calcShippingCharges1) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for Standard_Shipping");
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges1 + " and Calculated "+ calcShippingCharges1);
		
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("One_Day_Shipping")).click();
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double shippingCharges2 = TestUtil.getShippingCharges();
		System.out.println(shippingCharges2);
		double calcShippingCharges2 = TestUtil.calcOneDayShippingCharges("AK", false, 1, 2, 0);
		System.out.println(calcShippingCharges2);
		
		if (shippingCharges2 == calcShippingCharges2) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for Standard_Shipping");
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges2 + " and Calculated "+ calcShippingCharges2);
	
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("Standard_Shipping")).click();
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double shippingCharges3 = TestUtil.getShippingCharges();
		System.out.println(shippingCharges3);
		double calcShippingCharges3 = TestUtil.calcStandardShippingCharges("AK", false, 1, 2, 0);
		System.out.println(calcShippingCharges3);
		
		if (shippingCharges3 == calcShippingCharges3) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for Standard_Shipping");
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges3 + " and Calculated "+ calcShippingCharges3);
		
		
		Thread.sleep(1000);
		getObject("SUBMIT_ORDER").click();

		String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
		if (confMsg.contains("Order Confirmation")) {
			APPLICATION_LOG.debug("ExeComments --- Order Successful");
		} else {
			APPLICATION_LOG.debug("ERROR --- Order Confirmation Check Failed");
			fail("Order Confirmation Check Failed - Order Confirm issue");
		}
		String orderConfirmNum = getObject("ORDER_CONFIRM_NUMBER").getText();
		APPLICATION_LOG.debug("Order Confirmation Number: " + orderConfirmNum);
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
	

