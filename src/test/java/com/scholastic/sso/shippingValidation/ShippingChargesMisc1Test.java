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

public class ShippingChargesMisc1Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void shippingChargesMisc1Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing - Shipping Charges Misc1 Test ********");
		validateRunmodes("ShippingChargesMisc1Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		
		TestUtil.LoginTest();
//		Thread.sleep(15000);
//		TestUtil.clearCart();
//		TestUtil.signOut();
	
		TestUtil.selectBooks(1, true);
		getObject("CHECKOUT_NOW").click();
//		getObjectByCSS("[class=CheckoutButton]")
		Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
		TestUtil.LoginTestSce1 ();
		getObject("SHIPPING_CONTINUE").click();
		
		Thread.sleep(3000);
		String dollars = getObject("MINICART_SCHARGES").getText();
		Double actPrice = Double.parseDouble(dollars.substring(1, dollars.length()));
		System.out.println(actPrice);
		double shippingCharges = TestUtil.calcStandardShippingCharges("NJ", false, 1, 1, 0);
		System.out.println(shippingCharges);
		
		if (actPrice == shippingCharges){
			APPLICATION_LOG.debug("Standard_Shipping Charges matching for 1 book less than $35.00 and NJ State");
		}
		getObject("MINICART_EDIT").click();
		
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
			APPLICATION_LOG.debug("Format from the Pop Up - Iteration " + (i + 1)+ " - " + format);
			Thread.sleep(1000);

			
				WebElement qtyBox = driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4));
				qtyBox.clear();
				qtyBox.sendKeys("3");
				driver.findElement(By.xpath(itmPath5 + (i + 1) + itmPath6)).click();
				Thread.sleep(1000);
//				driver.findElement(By.xpath(itmPath3 + (i + 1) + itmPath4)).sendKeys("2");
			}
		getObject("EDIT_CART_CLOSE").click();
		
		double shippingCharges1 = TestUtil.getShippingChargesFromMiniCart();
		APPLICATION_LOG.debug("Shipping Charges from appication: " + shippingCharges1);
		double calcShippingCharges = 0.00;
		APPLICATION_LOG.debug("Shipping Charges from function: " + calcShippingCharges);
		
		if (shippingCharges1 == calcShippingCharges){
			APPLICATION_LOG.debug("Standard_Shipping Charges Matching for 3 Books");
		}else{
			APPLICATION_LOG.debug("Shipping Charges NOT matching at line 88");
			fail("Standard_Shipping Charges NOT Matching for 3 Books");
		}
		
		Thread.sleep(2000);
		getObject("BILLING_CONTINUE").click();
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
