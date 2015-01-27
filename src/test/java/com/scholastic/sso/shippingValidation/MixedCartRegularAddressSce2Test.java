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
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.util.TestUtil;

public class MixedCartRegularAddressSce2Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void mixedCartRegularAddressSce2Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing -  Mixed Cart Regular Address Scenario1 Test ********");
		validateRunmodes("MixedCartRegularAddressSce2Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		
		TestUtil.selectBooks(2, false);
		getObject("CHECKOUT_NOW").click();
		
		if (driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed()) {
			fail("ExeComments --- Free Shipping displaying for Books Order less than $35.00");
		} else if (driver.findElement(By.id("Standard_Shipping")).isSelected()) {
			TestUtil.shippingAddress("418 JFK Blvd", "Jersey City",
					"NJ - New Jersey", "07306");
			getObject("SHIPPING_CONTINUE").click();
			TestUtil.billingAddress();
Thread.sleep(2000);
			double shippingCharges = TestUtil.getShippingCharges();
			double calcShippingCharges = TestUtil.calcStandardShippingCharges(
					"NJ - New Jersey", false, 1, 2, 0);

			if (shippingCharges == calcShippingCharges) {
				APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING!!! :)");
			} else {
				APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :(");
			}

			APPLICATION_LOG.debug("ExeComments --- " + shippingCharges + " and Calculated "+ calcShippingCharges);
		} else {
			fail("ExeComments --- Standard_Shipping NOT displaying for Books Order less than $35.00");
		}

		getObject("HOME_IMAGE").click();
		double toysWeight = TestUtil.selectToys(2);
		getObject("CHECKOUT_NOW").click();

		if (driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed()) {
			fail("ExeComments --- Free Shipping displaying for Books Order less than $35.00");
		} else if (driver.findElement(By.id("Standard_Shipping")).isSelected()) {
			getObject("SHIPPING_CONTINUE").click();
			TestUtil.billingAddress();
			double shippingCharges = TestUtil.getShippingCharges();
			double calcShippingCharges = TestUtil.calcStandardShippingCharges(
					"NJ - New Jersey", false, 2, 2, toysWeight);
			if (! (shippingCharges == calcShippingCharges)){
				fail("Shipping Charges NOT matching after adding only Toys");
			}
			APPLICATION_LOG.debug("ExeComments --- " + shippingCharges + " and Calculated "
					+ calcShippingCharges);
		} else {
			fail("ExeComments --- Standard_Shipping NOT displaying for Books Order less than $35.00");
		}

		getObject("EDIT_CART").click();

		TestUtil.popUpHandler();
		Thread.sleep(3000);
		int editCartItems = TestUtil.popupItems();
		
		int count = 0;
		for (int i = 0; i < editCartItems; i++) {
			String itmPath1 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath2 = "]/td[2]/div/div[2]";
			Thread.sleep(3000);
			String format = driver.findElement(
					By.xpath(itmPath1 + (i + 1) + itmPath2)).getText();
			APPLICATION_LOG.debug("ExeComments --- Format from the Pop Up - Iteration " + (i + 1)
					+ " - " + format);
			Thread.sleep(3000);

			if (!bookFormats.contains(format))
			{
				Thread.sleep(3000);
				String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
				String itmPath4 = "]/td[3]/div/div[2]/a";
				Thread.sleep(3000);
				driver.findElement(
						By.xpath(itmPath3 + (i + 1 - count) + itmPath4))
						.click();
				Thread.sleep(3000);
				count++;
			}
		}
		
		getObject("EDIT_CART_CLOSE").click();
		Thread.sleep(3000);
		getObject("EDIT_CART").click();

		TestUtil.popUpHandler();
		int editCartItems1 = TestUtil.popupItems();
		APPLICATION_LOG.debug("ExeComments --- Cart Items count after removing Toys: "+ editCartItems1);

		for (int i = 0; i < editCartItems1; i++) {
			String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath4 = "]/td[3]/div/div[1]/input";
			String itmPath5 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath6 = "]/td[5]/span";
//			Thread.sleep(1000);
			driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4)).clear();
//			Thread.sleep(1000);
			driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4)).sendKeys("2");
			driver.findElement(By.xpath(itmPath5 + (i + 1) + itmPath6)).click();
			Thread.sleep(1000);
		}
		getObject("OVERLAY_CONTINUE").click();

		Thread.sleep(2000);
		
		getObject("SHIPPING_EDIT").click();
		if (driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed()) {
			APPLICATION_LOG.debug("ExeComments --- Free Shipping displaying for updated Books Order which is greater than $35.00");
		} else {
			fail("Free Shipping option not displaying for the updated order");
		}

		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();

		Thread.sleep(3000);
		String shipCharge = getObject("SHIP_CHARGES").getText();
		APPLICATION_LOG.debug("ExeComments --- Mini Cart Shipping Charges captured: " + shipCharge);
		if (shipCharge.contains("$0.00")) {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges are $0.00 for the Free Shipping method selected");
		} else {
			fail("Shipping Charges are NOT $0.00 for the Free Shipping method selected");
		}

		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("Standard_Shipping")).click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		Thread.sleep(5000);

		double shippingCharges1 = TestUtil.getShippingCharges();
		double calcShippingCharges1 = TestUtil.calcStandardShippingCharges(
				"NJ - New Jersey", false, 1, 4, 0);

		if (shippingCharges1 == calcShippingCharges1) {
			System.out
					.println("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for Standard_Shipping");
			fail("Shipping Charges NOT matching :( for Standard_Shipping");
		}

		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges1 + " and Calculated "
				+ calcShippingCharges1);

		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("Two_Day_Shipping")).click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		Thread.sleep(5000);
		
		double shippingCharges2 = TestUtil.getShippingCharges();
		double calcShippingCharges2 = TestUtil.calcTwoDayShippingCharges(
				"NJ - New Jersey", false, 1, 4, 0);

		if (shippingCharges2 == calcShippingCharges2) {
			System.out
					.println("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for 2-Day Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for 2 Day Shipping");
			fail("Shipping Charges NOT matching :( for 2 Day Shipping");
		}

		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges2 + " and Calculated "
				+ calcShippingCharges2);
		
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("One_Day_Shipping")).click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		Thread.sleep(5000);
		
		double shippingCharges3 = TestUtil.getShippingCharges();
		double calcShippingCharges3 = TestUtil.calcOneDayShippingCharges("NJ - New Jersey", false, 1, 4, 0);

		if (shippingCharges3 == calcShippingCharges3) {
			APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for 1-Day Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for 1-Day Shipping");
			fail("Shipping Charges NOT matching :( for 1-Day Shipping");
		}

		APPLICATION_LOG.debug("ExeComments --- " + shippingCharges3 + " and Calculated "+ calcShippingCharges3);
				
		getObject("EDIT_CART").click();

		TestUtil.popUpHandler();

		WebElement editCartPP2 = getObject("ITEMS_TABLE");
		List<WebElement> editCartItems2 = editCartPP2.findElements(By.tagName("img"));
		APPLICATION_LOG.debug("ExeComments --- After removing toys these many books left: "+ editCartItems2.size());

		for (int i = 0; i < editCartItems2.size(); i++) {
			String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath4 = "]/td[3]/div/div[1]/input";
			String itmPath5 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath6 = "]/td[5]/span";
			Thread.sleep(1000);
			driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4)).clear();
			driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4)).sendKeys("1");
			driver.findElement(By.xpath(itmPath5 + (i + 1) + itmPath6)).click();
		}
		getObject("OVERLAY_CONTINUE").click();
		getObject("SHIPPING_EDIT").click();

		if (driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected()) {
			fail("ExeComments --- Free Shipping displaying for Books Order less than $35.00");
		} else {
			getObject("SHIPPING_CONTINUE").click();
			getObject("BILLING_CONTINUE").click();
			Thread.sleep(1000);
			
			APPLICATION_LOG.debug("Checkpoint 1");
			
			double shippingCharges4 = TestUtil.getShippingCharges();
			double calcShippingCharges4 = TestUtil.calcStandardShippingCharges("NJ - New Jersey", false, 1, 4, 0);

			if (shippingCharges4 == calcShippingCharges4) {
				APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for 1-Day Shipping with 3 Books");
			} else {
				APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :( for 1-Day Shipping");
				fail("Shipping Charges NOT matching :( for 1-Day Shipping");
			}

			APPLICATION_LOG.debug("ExeComments --- " + shippingCharges4 + " and Calculated "+ calcShippingCharges4);
			
			Thread.sleep(1000);
			getObject("SUBMIT_ORDER").click();

			String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
			if (confMsg.contains("Order Confirmation")) {
				APPLICATION_LOG.debug("ExeComments --- Order Successful");
			} else {
				APPLICATION_LOG.debug("ERROR --- Order Confirmation Check Failed");
				fail("Order Confirmation Check Failed - Order Confirm issue");
			}
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
