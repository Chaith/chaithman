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

public class ShippingChargesMisc3Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void shippingChargesMisc3Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing -  Shipping Charges Misc3 Test ********");
		validateRunmodes("ShippingChargesMisc3Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		TestUtil.selectBooks(2, false);
		getObject("CHECKOUT_NOW").click();
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
		TestUtil.LoginTestSce1 ();
		getObject("CHECKOUT_CONTINUE_SHOPPING").click();
		double toyWeight = TestUtil.selectToys(2);
		getObject("CHECKOUT_NOW").click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		getObject("MINICART_EDIT").click();
		
		TestUtil.popUpHandler();
//		Thread.sleep(3000);
		int editCartItems = TestUtil.popupItems();
		
		int count = 0;
		for (int i = 0; i < editCartItems; i++) {
			String itmPath1 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath2 = "]/td[2]/div/div[2]";
			Thread.sleep(3000);
			String format = driver.findElement(
					By.xpath(itmPath1 + (i + 1) + itmPath2)).getText();
			APPLICATION_LOG.debug("ExeComments --- Format from the Pop Up - Iteration " + (i + 1)+ " - " + format);
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
		
		double shippingCharges = TestUtil.getShippingCharges();
		System.out.println(shippingCharges);
		double calcShippingCharges = TestUtil.calcStandardShippingCharges("NJ - New Jersey", false, 1, 2, 0);
		System.out.println(calcShippingCharges);
		
		if (shippingCharges != calcShippingCharges){
			APPLICATION_LOG.debug("Shipping Charges NOT matching at line 88");
			fail("Standard_Shipping Charges NOT Matching for 2 Books after removing Toys");
		}
		
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
