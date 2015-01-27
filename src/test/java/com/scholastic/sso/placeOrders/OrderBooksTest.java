package com.scholastic.sso.placeOrders;
	
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.util.TestUtil;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.TestBase;
	
	public class OrderBooksTest extends TestBase{
	
		@BeforeMethod
		public void beforeTest() throws IOException, InterruptedException {
//			Thread.sleep(5000);
			initialize();
		}
//		@Test(dataProvider="getData")
		@Test(dataProviderClass=TestDataProvider.class, dataProvider="PlaceOrdersDataProvider")
//		public void test1(String col1, String col2, String col3, String col4, String col5){
		public void orderBooksTest(Hashtable<String, String> table) throws IOException, InterruptedException{
			double actPrice = 0.0;
			APPLICATION_LOG.debug("******** Executing - Order Books Test ********");
			 
			/*
			 Xls_Reader xls = new Xls_Reader("C:\\Users\\chaitman\\Desktop\\SSO\\SSOFwk\\Suite.xlsx");
			System.out.println(Utility.isSuiteRunnable("SuiteA", xls));
			System.out.println(Utility.isSuiteRunnable("SuiteB", xls));
			System.out.println(Utility.isSuiteRunnable("SuiteC", xls));
			Xls_Reader xls1 = new Xls_Reader("C:\\Users\\chaitman\\Desktop\\SSO\\SSOFwk\\SuiteA.xlsx");
			System.out.println(Utility.isTestCaseRunnable("Test1", xls1));
		table.get("Runmode");
			 */
			System.out.println("Screenshot" + OR.getProperty("SCREENSHOT_DIR"));
			validateRunmodes("OrderBooksTest", Constants.PLACEORDERS_SUITE, table.get(Constants.RUNMODE_COL));
			
			TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
			
			Assert.assertEquals(driver.getTitle(), OR.getProperty("HOME_PAGE_TITLE"), "Titles NOT matching");
			actPrice = TestUtil.checkFreeShipping();
//			TestUtil.selectBooks(2, true);
			Thread.sleep(2000);
			getObject("CHECKOUT_NOW").click();
			Assert.assertTrue(driver.findElement(By.id("Free_Standard_Shipping_(Books)")).isSelected());
			Assert.assertTrue(driver.findElement(By.id("Standard_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("Two_Day_Shipping")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.id("One_Day_Shipping")).isDisplayed());

			TestUtil.shippingAddress("418 JFK Blvd", "Jersey City", "NJ - New Jersey", "07306");
			getObject("SHIPPING_CONTINUE").click();
			TestUtil.billingAddress();
			Thread.sleep(5000);
			String taxCaptured = getObject("TAX_APPLIED").getText();
			Double taxAdded = Double.parseDouble(taxCaptured.substring(1,taxCaptured.length()));
			APPLICATION_LOG.debug("ExeComments --- Tax applied for the State selected: " + taxAdded);

			Double calOrderTotal = actPrice + taxAdded;

			DecimalFormat df = new DecimalFormat("#.##");
			Double testOrderTotal = Double.valueOf(df.format(calOrderTotal));
			APPLICATION_LOG.debug("ExeComments --- Calculated Order Total from the Test: " + testOrderTotal);

			String ckPrice = getObject("ORDER_AMOUNT").getText();
			Double appOrderTotal = Double.parseDouble(ckPrice.substring(1,ckPrice.length()));
			APPLICATION_LOG.debug("ExeComments --- Order Total from the Store: " + appOrderTotal);

			if (appOrderTotal.equals(testOrderTotal)) {
				APPLICATION_LOG.debug("ExeComments --- Order Totals Matched in both Test and Application");
			} else {
				APPLICATION_LOG.debug("ERROR --- TEST CASE FAILED - Order totals dont match --- ");
				fail("Order Totals from both Test and Store dont match - Pricing Issue");
			}

			getObject("SUBMIT_ORDER").click();
			String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
			// Assert.assertTrue("Submit Order Failed", confMsg.contains("Order Confirmation"));
			if (confMsg.contains("Order Confirmation")) {
				APPLICATION_LOG.debug("ExeComments --- Order Successful");
			} else {
				fail("Order Confirmation Check Failed - Order Confirm issue");
				APPLICATION_LOG.debug("ERROR --- Order Confirmation Check Failed");
			}

			if (CONFIG.getProperty("CREATE_ACCOUNT").equals("Yes")) {
				TestUtil.registerUser();
			} else {
				APPLICATION_LOG.debug("ExeComments --- Registration step not specified");
			}

		}
		
//		@DataProvider
//		public Object[][] getData(){
//			Xls_Reader xls1 = new Xls_Reader("C:\\Users\\chaitman\\Desktop\\SSO\\SSOFwk\\SuiteA.xlsx");
//			
//			return Utility.getData("test1", xls1);
//		}
		
//		@AfterMethod
//		public static void takeScreenShotOnFailure(ITestResult testResult)throws IOException {
//			if (testResult.getStatus() == ITestResult.FAILURE) {
//				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//				System.out.println(testResult.getName());
//				String screenShotFileName = CONFIG.getProperty("SCREENSHOT_DIR") + "/" + testResult.getName() + ".jpg";
//				FileUtils.copyFile(scrFile, new File(screenShotFileName));
//			}
//		}
		
		@AfterMethod
		public void shutDriver() throws InterruptedException {

			try {
				dispose();
			} catch (Throwable t) {
				return;
			}
		}
}
