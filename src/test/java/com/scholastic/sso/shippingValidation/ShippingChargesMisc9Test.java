package com.scholastic.sso.shippingValidation;

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

public class ShippingChargesMisc9Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void shippingChargesMisc9Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing -  Shipping Charges Misc9 Test ********");
		validateRunmodes("ShippingChargesMisc9Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		TestUtil.selectBooks(2, true);
		getObject("CHECKOUT_NOW").click();
		
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		TestUtil.shippingAddress("123 JFK Blvd", "Jersey City", "NJ - New Jersey", "07306");
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		getObject("SHIPPING_CONTINUE").click();
		TestUtil.billingAddress();
		APPLICATION_LOG.debug("Done");
//		getObject("BILLING_CONTINUE").click();
		getObject("SHIPPING_EDIT_OTHER").click();
		
		getObject("DELETE_ADDRESS").click();
		Thread.sleep(1000);
		TestUtil.shippingAddressPOBox();
		
		Thread.sleep(1000);
//		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
//		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertFalse(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertFalse(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		APPLICATION_LOG.debug("Check point 0");
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		getObject("SHIPPING_EDIT").click();
		
		APPLICATION_LOG.debug("Check point 1");
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		APPLICATION_LOG.debug("Check point 1.1");
		Thread.sleep(1000);
//		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		
		APPLICATION_LOG.debug("Check point 2");
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		APPLICATION_LOG.debug("Check point 3");
		
		double shippingCharges = TestUtil.getShippingCharges();
		double calcShippingCharges = 0.00;
		if (shippingCharges == calcShippingCharges) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for Standard_Shipping");
			fail("Shipping Charges NOT matching");			
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges + " and Calculated "+ calcShippingCharges);
		
		getObject("SUBMIT_ORDER").click();
		String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
		// Assert.assertTrue("Submit Order Failed",
		// confMsg.contains("Order Confirmation"));
		if (confMsg.contains("Order Confirmation")) {
			APPLICATION_LOG.debug("ExeComments --- Order Successful");
		} else {
			APPLICATION_LOG.debug("ERROR --- Order Confirmation Check Failed");
			fail("Order Confirmation Check Failed - Order Confirm issue");
		}
		String orderConfirmNum = getObject("ORDER_CONFIRM_NUMBER").getText();
		APPLICATION_LOG.debug("Order Confirmation Number: " + orderConfirmNum);
		
  }

	private void fail(String string) {
		// TODO Auto-generated method stub
		
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

		