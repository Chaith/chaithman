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

public class ShippingChargesMisc5Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void shippingChargesMisc5Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing -  Shipping Charges Misc5 Test ********");
		validateRunmodes("ShippingChargesMisc5Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		TestUtil.selectBooks(2, false);
		getObject("CHECKOUT_NOW").click();
		
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		TestUtil.LoginTestSce1 ();
		TestUtil.selectAddress("AA");
		
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
		Assert.assertFalse(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertFalse(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double shippingCharges = TestUtil.getShippingCharges();
		System.out.println(shippingCharges);
		double calcShippingCharges = TestUtil.calcStandardShippingCharges("AA", false, 1, 2, 0);
		System.out.println(calcShippingCharges);
		
		if (shippingCharges == calcShippingCharges) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for Standard_Shipping");
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges + " and Calculated "+ calcShippingCharges);
		
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
	
