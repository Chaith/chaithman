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

public class ShippingChargesMisc8Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void shippingChargesMisc8Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing -  Shipping Charges Misc8 Test ********");
		validateRunmodes("ShippingChargesMisc8Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		TestUtil.selectBooks(2, true);
		getObject("CHECKOUT_NOW").click();
		
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		TestUtil.shippingAddressPOBox();
		Thread.sleep(2000);
		Assert.assertFalse(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertFalse(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
//		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
//		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		
		getObject("SHIPPING_CONTINUE").click();
		
		double shippingCharges = TestUtil.getShippingChargesFromMiniCart();
		double calcShippingCharges = 0.00;
		if (shippingCharges == calcShippingCharges) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Free Shipping with 2 Books worth >$35.00");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching for Free Shipping for Two Books worth <$35.00");
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges + " and Calculated "+ calcShippingCharges);
		
		getObject("MINICART_EDIT").click();
		
		TestUtil.popUpHandler();
			getObject("REMOVE_ITEM1").click();	
			
		Thread.sleep(6000);
			
		getObject("EDIT_CART_CLOSE").click();
		
		Thread.sleep(6000);
		
		getObject("SHIPPING_EDIT").click();
		
		
		getObject("DELETE_ADDRESS").click();
		TestUtil.shippingAddress("123 JFK Blvd", "Jersey City", "NJ - New Jersey", "07306");
		
		
		Thread.sleep(6000);
//		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed());
//		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
		
		Thread.sleep(6000);
		
		getObject("SHIPPING_CONTINUE").click();
		
		Thread.sleep(6000);
		TestUtil.billingAddress();
		
		Thread.sleep(6000);
//		getObject("BILLING_CONTINUE").click();

		double shippingCharges1 = TestUtil.getShippingCharges();
//		System.out.println(shippingCharges1);
		double calcShippingCharges1 = TestUtil.calcStandardShippingCharges("NJ - New Jersey", false, 1, 1, 0);
//		System.out.println(calcShippingCharges1);
		if (shippingCharges1 == calcShippingCharges1) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 1 Book worth <$35.00");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching for Standard_Shipping for 1 Book worth <$35.00: "+ shippingCharges1 +" and " + calcShippingCharges1);
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}
		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges1 + " and Calculated "+ calcShippingCharges1);
		
		getObject("SUBMIT_ORDER").click();
		String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
		if (confMsg.contains("Order Confirmation")) {
			APPLICATION_LOG.debug("ExeComments --- Order Successful");
		} else {
			APPLICATION_LOG.debug("ERROR --- Order Confirmation Check Failed");
			fail("Order Confirmation Check Failed - Order Confirm issue");
		}
		String orderConfirmNum = getObject("ORDER_CONFIRM_NUMBER").getText();
		APPLICATION_LOG.debug("ExeComments --- Order Confirmation Number: " + orderConfirmNum);
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
