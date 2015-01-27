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
import org.openqa.selenium.WebElement;
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

public class MixedCartPOBoxAddressSce3Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void mixedCartPOBoxAddressSce3Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing -  Mixed Cart PO Box Address Scenario3 Test ********");
		validateRunmodes("MixedCartPOBoxAddressSce3Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));

		TestUtil.selectBooks(2, true);
		getObject("CHECKOUT_NOW").click();
		
		TestUtil.shippingAddressPOBox();
		
//		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected(), "Free Shipping selected for order > $35.00");
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("POBox_Standard_Shipping")).isDisplayed());
		Assert.assertFalse(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed(), "Two_Day_Shipping displaying for PO Box address");
		Assert.assertFalse(driver.findElement(By.id("One_Day_Shipping")).isDisplayed(), "One_Day_Shipping displaying for PO Box address");
		
		driver.findElement(By.id("POBox_Standard_Shipping")).click();
		
		getObject("SHIPPING_CONTINUE").click();
		TestUtil.billingAddress();
		getObject("CHECKOUT_CONTINUE_SHOPPING").click();

		double toysWeight = TestUtil.selectToys(2);
		getObject("CHECKOUT_NOW").click();
		
		Assert.assertFalse(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed(), "Free Standard_Shipping displaying for Mixed cart");

		getObject("SHIPPING_CONTINUE").click();
		TestUtil.billingAddress();
		Thread.sleep(7000);
		double shippingCharges = TestUtil.getShippingCharges();
		double calcShippingCharges = TestUtil.calcStandardShippingCharges("VA - Virginia", true, 2, 2, toysWeight);
		
		if (shippingCharges == calcShippingCharges){
			APPLICATION_LOG.debug("Charges matching for a Mixed Cart " + shippingCharges + " -- " + calcShippingCharges);
		}else {
			APPLICATION_LOG.debug("Charges NOT matching for a Mixed Cart " + shippingCharges + " -- " + calcShippingCharges);
		}
		
		getObject("EDIT_CART").click();
		
		TestUtil.popUpHandler();
		int editCartItems = TestUtil.popupItems();
		int count = 0;
		for (int i = 0; i < editCartItems; i++) {
			String itmPath1 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath2 = "]/td[2]/div/div[2]";
			Thread.sleep(2000);
			String format = driver.findElement(By.xpath(itmPath1 + (i + 1) + itmPath2)).getText();
			Thread.sleep(1000);

			if (!bookFormats.contains(format))
			{
				Thread.sleep(1000);
				String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
				String itmPath4 = "]/td[3]/div/div[2]/a";
				Thread.sleep(1000);
				driver.findElement(By.xpath(itmPath3 + (i + 1 - count) + itmPath4)).click();
				Thread.sleep(1000);
				count++;
			}else{
				String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
				String itmPath4 = "]/td[3]/div/div[1]/input";
				String itmPath5 = "//*[@id='ItemsTable']/tbody/tr[";
				String itmPath6 = "]/td[5]/span";
				WebElement qtyBox = driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4));
				qtyBox.clear();
				qtyBox.sendKeys("2");
				driver.findElement(By.xpath(itmPath5 + (i + 1) + itmPath6)).click();
				Thread.sleep(1000);
			}
		}
		
		getObject("EDIT_CART_CLOSE").click();
		getObject("SHIPPING_EDIT").click();
		
		Thread.sleep(2000);
		
		Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected(), "Free Shipping is selected after removing Toys and for Books Order which is greater than $35.00");

		getObject("SHIPPING_CONTINUE").click();
		Thread.sleep(2000);
		getObject("BILLING_CONTINUE").click();
		double shippingCharges1 = TestUtil.getShippingCharges();
		
//		Assert.assertTrue(shippingCharges1 == 0.00);
		if (shippingCharges1 == 0.00){
			APPLICATION_LOG.debug("No Shiping charges for Books more than $35.00 " + shippingCharges1 + " -- " + "0.00");
		}else{
			fail("Shipping charges applied for only Books more than $35.00 " + shippingCharges1 + " -- " + "0.00");
		}
		
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("POBox_Standard_Shipping")).click();
		
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double shippingCharges2 = TestUtil.getShippingCharges();
		double calcShippingCharges2 = TestUtil.calcStandardShippingCharges("VA - Virginia", true, 1, 4, 0);
		
		if (shippingCharges2 == calcShippingCharges2){
			APPLICATION_LOG.debug("Standard_Shipping Charges Matching for 4 Books " + shippingCharges2 + " -- " + calcShippingCharges2);
		}else{
			APPLICATION_LOG.debug("Standard_Shipping Charges NOT Matching for 4 Books " + shippingCharges2 + " -- " + calcShippingCharges2);
			fail("Standard_Shipping Charges NOT Matching for 3 Books");
		}
		
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("Free_Standard_Shipping_(Books)")).click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
double shippingCharges3 = TestUtil.getShippingCharges();
		
		if (shippingCharges3 == 0.00){
			APPLICATION_LOG.debug("No Shiping charges for Books more than $35.00 i.e. 4 books " + shippingCharges3 + " -- " + "0.00");
		}else{
			APPLICATION_LOG.debug("Shiping charges applied for Books more than $35.00 i.e. 4 books " + shippingCharges3 + " -- " + "0.00");
			fail("Shipping charges applied for only Books more than $35.00");
		}
		
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
