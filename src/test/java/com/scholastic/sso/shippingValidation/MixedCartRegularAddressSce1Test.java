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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.util.TestUtil;
import com.scholastic.sso.util.Xls_Reader;
	
	public class MixedCartRegularAddressSce1Test extends TestBase{
		
		@BeforeTest
		public void beforeTest() throws IOException, InterruptedException {
			initialize();
		}
	
		@Test(dataProviderClass=TestDataProvider.class, dataProvider="ShippingValidationDataProvider")
		public void mixedCartRegularAddressSce1Test(Hashtable<String, String> table) throws IOException, InterruptedException{
			
			List<String> bookFormats = new ArrayList<String>();
			bookFormats.add("Storia eBooks");
			bookFormats.add("Hardcover");
			bookFormats.add("Paperback");
			bookFormats.add("Audio CD");
			bookFormats.add("Board Book");
			
			APPLICATION_LOG.debug("******** Executing -  Mixed Cart Regular Address Scenario1 Test ********");
			validateRunmodes("MixedCartRegularAddressSce1Test", Constants.SHIPPINGVALIDATION_SUITE, table.get(Constants.RUNMODE_COL));
			
			TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
			
			TestUtil.selectBooks(2, true);
			
			getObject("CHECKOUT_NOW").click();
			
			Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected(), "Free Shipping Selected after coming to the Checkout page by adding book value > $35.00");
			Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
			
			Thread.sleep(3000);
			TestUtil.LoginTestSce1();
			Thread.sleep(3000);
			Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
			Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
			
//			try{
//			Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
//			}catch(Throwable t){
//				APPLICATION_LOG.debug("One_Day_Shipping - option NOT displaying");
//			}
			
			getObject("SHIPPING_CONTINUE").click();
			Thread.sleep(3000);
			getObject("BILLING_CONTINUE").click();
			Thread.sleep(3000);
			getObject("CHECKOUT_CONTINUE_SHOPPING").click();
			
//			TestUtil.goToStore(CONFIG.getProperty("STAGE_URL"));
			Thread.sleep(3000);
			Double toysWeight = TestUtil.selectToys(2);
			getObject("CHECKOUT_NOW").click();
			
			Thread.sleep(3000);
			Assert.assertFalse(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
			Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
			Thread.sleep(3000);
			getObject("SHIPPING_CONTINUE").click();
			Thread.sleep(3000);
			getObject("BILLING_CONTINUE").click();
			Thread.sleep(3000);
			int bookCnt = 0;
			bookCnt = TestUtil.getBooksCount();
			APPLICATION_LOG.debug("Books count after calling the function getBooksCount: " + bookCnt);
			Thread.sleep(3000);
			double shippingCharges = TestUtil.getShippingCharges();
			double calcShippingCharges = TestUtil.calcStandardShippingCharges("NJ - New Jersey", false, 2, bookCnt, toysWeight);
			
			if (shippingCharges == calcShippingCharges){
				APPLICATION_LOG.debug("ExeComments --- Shipping charges matching: " + shippingCharges +" -- " +calcShippingCharges);
			}else{
				APPLICATION_LOG.debug("ExeComments --- Shipping charges are NOT matching: " + shippingCharges +" -- " +calcShippingCharges);
			}
			
			getObject("EDIT_CART").click();
			Thread.sleep(3000);
			int editCartItems = TestUtil.popupItems();
			int count = 0;
			for (int i = 0; i < editCartItems; i++) {
				String itmPath1 = "//*[@id='ItemsTable']/tbody/tr[";
				String itmPath2 = "]/td[2]/div/div[2]";
				Thread.sleep(1000);
				String format = driver.findElement(By.xpath(itmPath1 + (i + 1) + itmPath2)).getText();
				if (!bookFormats.contains(format)) {
					Thread.sleep(1000);
					String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
					String itmPath4 = "]/td[3]/div/div[2]/a";
					Thread.sleep(1000);
					driver.findElement(By.xpath(itmPath3 + (i + 1 - count) + itmPath4)).click();
					Thread.sleep(1000);
					count++;
				} else {
					String itmPath3 = "//*[@id='ItemsTable']/tbody/tr[";
					String itmPath4 = "]/td[3]/div/div[1]/input";
					String itmPath5 = "//*[@id='ItemsTable']/tbody/tr[";
					String itmPath6 = "]/td[5]/span";
//					Thread.sleep(1000);
					WebElement qtyBox = driver.findElement(By.xpath(itmPath3+ (i + 1) + itmPath4));
//					Thread.sleep(3000);
					qtyBox.clear();
//					Thread.sleep(3000);
					qtyBox.sendKeys("2");
//					Thread.sleep(3000);
					driver.findElement(By.xpath(itmPath5 + (i + 1) + itmPath6)).click();
					Thread.sleep(3000);
				}
			}
			
			getObject("EDIT_CART_CLOSE").click();
			Thread.sleep(1000);
			APPLICATION_LOG.debug("Remove Toys - DONE");
			
			double shippingCharges1 = TestUtil.getShippingCharges();
			double calcShippingCharges1 = 0.00; // i.e. Expected Shipping Charges after removing Toys from the Cart and the value of the Books being >$35.00
			if (shippingCharges1 == calcShippingCharges1){
				APPLICATION_LOG.debug("Shipping charges mathcing after Removing Toys: " + shippingCharges1 +" -- " +calcShippingCharges1);
			}else{
				APPLICATION_LOG.debug("Shipping charges NOT mathcing after Removing Toys: " + shippingCharges1 +" -- " +calcShippingCharges1);
			}
			
			Thread.sleep(3000);
			getObject("SHIPPING_EDIT").click();
			
			Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
			Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
			
			getObject("SHIPPING_CONTINUE").click();
			getObject("BILLING_CONTINUE").click();
			Thread.sleep(3000);
			getObject("EDIT_CART").click();
			
			TestUtil.popUpHandler();
			
			Thread.sleep(1000);
			getObject("REMOVE_ITEM_IN_CART").click();
			getObject("ITEM_QTY_IN_CART").clear();
			getObject("ITEM_QTY_IN_CART").sendKeys("1");
			getObject("ITEM_PRICE").click();
			Thread.sleep(1000);
			getObject("EDIT_CART_CLOSE").click();

			getObject("SHIPPING_EDIT").click();
			Thread.sleep(3000);
			Assert.assertFalse(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isSelected());
			Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());
			
			getObject("SHIPPING_CONTINUE").click();
			getObject("BILLING_CONTINUE").click();
			
			double shippingCharges2 = TestUtil.getShippingCharges();
			double calcShippingCharges2 = TestUtil.calcStandardShippingCharges("NJ - New Jersey", false, 1, 1, 0);
			if (shippingCharges2 == calcShippingCharges2){
				APPLICATION_LOG.debug("Standard_Shipping charges mathcing after removing changing Qty of Books to 1: " + shippingCharges2 +" -- " +calcShippingCharges2);
			}else{
				APPLICATION_LOG.debug("Shipping charges NOT matching after removing changing Qty of Books to 1: " + shippingCharges2 +" -- " +calcShippingCharges2);
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
