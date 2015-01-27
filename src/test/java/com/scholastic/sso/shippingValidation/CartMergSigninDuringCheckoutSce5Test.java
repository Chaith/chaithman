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

public class CartMergSigninDuringCheckoutSce5Test extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}

	@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
	public void cartMergSigninDuringCheckoutSce5Test(Hashtable<String, String> table) throws IOException, InterruptedException{
		
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		APPLICATION_LOG.debug("******** Executing - Cart Merg and SignIn During Chekout Test ********");
		validateRunmodes("CartMergSigninDuringCheckoutSce5Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		
		TestUtil.LoginTestSce1();
		Thread.sleep(15000);
		TestUtil.clearCart();
		TestUtil.selectBooks(1, true);
		double toyWeight = TestUtil.selectToys(1);
		TestUtil.signOut();
		
//		TestUtil.goToStore(CONFIG.getProperty("STAGE_URL"));
		TestUtil.selectBooks(1, false);
		getObject("CHECKOUT_NOW").click();
		TestUtil.shippingAddress("123 JFK Blvd", "Jersey City", "NJ - New Jersey", "07306");
		if (driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed()){
			fail("Free Shipping option displaying for orders less than $35.00");
		}
		if (!driver.findElement(By.id("Standard_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (!driver.findElement(By.id("Two_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (!driver.findElement(By.id("One_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		driver.findElement(By.id("Two_Day_Shipping")).click();
		getObject("SHIPPING_CONTINUE").click();
		TestUtil.billingAddress();
		
		double calShipPrice = TestUtil.calcTwoDayShippingCharges("NJ - New Jersey", false, 1, 1, 0);
		double appShipPrice = TestUtil.getShippingCharges();
		
		if (appShipPrice == calShipPrice) {APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :(");
		}
		APPLICATION_LOG.debug("ExeComments --- " + appShipPrice + " and Calculated "+ calShipPrice);
		
		getObject("SHIPPING_EDIT").click();
		TestUtil.LoginTestSce1 ();
		WebElement tab = driver.findElement(By.cssSelector("table[id='ItemsTable']"));
		List<WebElement> items = tab.findElements(By.cssSelector("td[class='quantity']"));
		APPLICATION_LOG.debug("Total items in the Cart after the login: " + items.size());
		getObject("CHECKOUT_NOW").click();
		
		Thread.sleep(6000);
		
		if (driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed()){
			fail("Free Shipping option displaying for orders less than $35.00");
		}
		if (!driver.findElement(By.id("Standard_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (!driver.findElement(By.id("Two_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (!driver.findElement(By.id("One_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		getObject("SHIPPING_CONTINUE").click();
//		Thread.sleep(2000);
//		TestUtil.billingAddressNoEmail();		
		getObject("BILLING_CONTINUE").click();
//		double toyWeight = 1.00;
		
		double calShipPrice1 = TestUtil.calcStandardShippingCharges("NJ - New Jersey", false, 2, 2, toyWeight);
//		double calShipPrice1 = TestUtil.calcTwoDayShippingCharges("NJ - New Jersey", false, 2, 2, toyWeight);
		APPLICATION_LOG.debug("Calculated Ship Cahrges: " + calShipPrice1);
		double appShipPrice1 = TestUtil.getShippingCharges();
		APPLICATION_LOG.debug("Ship Cahrges from Application: " + appShipPrice1);
		
		if (appShipPrice1 == calShipPrice1) {APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :(");
		}
		APPLICATION_LOG.debug("ExeComments --- " + appShipPrice1 + " and Calculated "+ calShipPrice1);
//		fail("2Day Shipping charges not matching after cart merg - mixed cart with a toy and 2 books");
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("Two_Day_Shipping")).click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double calShipPrice2 = TestUtil.calcTwoDayShippingCharges("NJ - New Jersey", false, 2, 2, toyWeight);
		APPLICATION_LOG.debug("Calculated Ship Cahrges: " + calShipPrice2);
		double appShipPrice2 = TestUtil.getShippingCharges();
		APPLICATION_LOG.debug("Ship Cahrges from Application: " + appShipPrice2);
		
		if (appShipPrice2 == calShipPrice2) {APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :(");
			fail("1Day Shipping charges not matching after cart merg - mixed cart with a toy and 2 books");
		}
		getObject("SHIPPING_EDIT").click();
		driver.findElement(By.id("Two_Day_Shipping")).click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		double calShipPrice3 = TestUtil.calcTwoDayShippingCharges("NJ - New Jersey", false, 2, 2, toyWeight);
		APPLICATION_LOG.debug("Calculated Ship Cahrges: " + calShipPrice3);
		double appShipPrice3 = TestUtil.getShippingCharges();
		APPLICATION_LOG.debug("Ship Cahrges from Application: " + appShipPrice3);
		
		if (appShipPrice3 == calShipPrice3) {APPLICATION_LOG.debug("ExeComments --- Both Calculated and Captured Shipping Charges MATCHING for Std Shipping with 3 Books");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Shipping Charges NOT matching :(");
			fail("1Day Shipping charges not matching after cart merg - mixed cart with a toy and 2 books");
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
			APPLICATION_LOG.debug("Format from the Pop Up - Iteration " + (i + 1)+ " - " + format);
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
			}
	}
		getObject("EDIT_CART_CLOSE").click();
		getObject("SHIPPING_EDIT").click();
		
		if (!driver.findElement(By.id("Standard_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		Thread.sleep(3000);
//		if (!driver.findElement(By.id("Two_Day_Shipping")).isSelected()){
		if (!driver.findElement(By.id("Two_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (!driver.findElement(By.id("One_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		
		Thread.sleep(2000);
		
		if (getObject("SHIPPING_EDIT_OTHER").isDisplayed()){
			getObject("SHIPPING_EDIT_OTHER").click();
		}
		Thread.sleep(2000);
		TestUtil.selectAddress("VA");
//		if (!driver.findElement(By.id("ShippingMode_6")).isDisplayed()){
//			fail("Standard_Shipping option not displaying");
//		}
		if (driver.findElement(By.id("Two_Day_Shipping")).isDisplayed()){
			fail("Two Shipping option displaying fro PO Box address");
		}
		if (driver.findElement(By.id("One_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		
		TestUtil.selectAddress("NJ");
		if (!driver.findElement(By.id("Standard_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (!driver.findElement(By.id("Two_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (!driver.findElement(By.id("One_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		
		TestUtil.selectAddress("VA");
		if (!driver.findElement(By.id("POBox_Standard_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (driver.findElement(By.id("Two_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		if (driver.findElement(By.id("One_Day_Shipping")).isDisplayed()){
			fail("Standard_Shipping option not displaying");
		}
		
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		Thread.sleep(3000);
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
