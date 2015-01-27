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

public class ShippingChargesMisc2Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void shippingChargesMisc2Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		
		APPLICATION_LOG.debug("******** Executing -  Shipping Charges Misc2 Test ********");
		validateRunmodes("ShippingChargesMisc2Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		
		double toyWeight = TestUtil.selectToys(2);
		APPLICATION_LOG.debug("Toys Weight before increasing Qty: " + toyWeight);
		double totToysWeight = toyWeight*2;
		APPLICATION_LOG.debug("Total Toy Weight: " + totToysWeight);
		APPLICATION_LOG.debug("Total Toys Weight for increased Qty: " + totToysWeight);
		getObject("CHECKOUT_NOW").click();
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
		TestUtil.LoginTestSce1 ();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		getObject("EDIT_CART").click();
		TestUtil.popUpHandler();
		int editCartItems = TestUtil.popupItems();
		int count = 0;
		for (int i = 0; i < editCartItems; i++) {
			String itmPath1 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath2 = "]/td[2]/div/div[2]";
			String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath4 = "]/td[3]/div/div[1]/input";
			String itmPath5 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath6 = "]/td[5]/span";
			Thread.sleep(2000);
			String format = driver.findElement(By.xpath(itmPath1 + (i + 1) + itmPath2)).getText();
//			APPLICATION_LOG.debug("Format from the Pop Up - Iteration " + (i + 1)+ " - " + format);
			Thread.sleep(1000);
			
				WebElement qtyBox = driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4));
				qtyBox.clear();
				qtyBox.sendKeys("2");
				driver.findElement(By.xpath(itmPath5 + (i + 1) + itmPath6)).click();
				Thread.sleep(1000);
			}
		
		getObject("EDIT_CART_CLOSE").click();
//		Double.parseDouble(shipCharge.substring(1, shipCharge.length()));
		double shippingCharges = TestUtil.getShippingCharges();
		System.out.println(shippingCharges);
		double calcShippingCharges = TestUtil.calcStandardShippingCharges("NJ - New Jersey", false, 0, 0, totToysWeight);
		System.out.println(calcShippingCharges);
		
		if (shippingCharges != calcShippingCharges){
			APPLICATION_LOG.debug("Shipping Charges NOT matching at line 88");
			fail("Standard_Shipping Charges NOT Matching for 4 Toys");
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
