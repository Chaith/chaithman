package com.scholastic.sso.misc;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.util.TestUtil;

public class AddEditDeleteCreditCardTest extends TestBase{

	@BeforeMethod()
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
		
		APPLICATION_LOG.debug("******** Executing -  Add, Update and Delete Credit Card Info Test ********");
	}
	
	@Test(priority=1, dataProviderClass=TestDataProvider.class, dataProvider="MiscDataProvider")
	public void addCreditCardInfoTest(Hashtable<String, String> table) throws InterruptedException, IOException {
		
		validateRunmodes("AddCreditCardInfoTest", Constants.MISC_SUITE, table.get(Constants.RUNMODE_COL));
	
	String expBCH1 = "Books";
	String expBCH2 = "Age 3 - 5";
	String expBCH3 = "Age 5";
	
	TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
	TestUtil.LoginCC();
	Thread.sleep(15000);
	getObject("SHOP_BY_AGE").click();
	Thread.sleep(3000);
	getObject("AGE3-5").click();
	String bcH1 = getObject("BREADCRUMB_H1").getText();
	Thread.sleep(3000);
	String bcH2 = getObject("BREADCRUMB_H2").getText();
	
	if (bcH1.equals(expBCH1) && bcH2.equals(expBCH2)){
		getObject("Age5").click();
	}
	
	String bcH3 = getObject("BREADCRUMB_H3").getText();
	if (bcH3.equals(expBCH3)){
		TestUtil.selectBooks(1, false);
		getObject("CHECKOUT_NOW").click();
	}
	
//	if (getObject("DELETE_ONLYADD").isDisplayed()){
//		getObject("DELETE_ONLYADD").click();	}
	
	TestUtil.shippingAddress("123 JFK", "Jersey City", "NJ - New Jersey", "07306");
	getObject("SHIPPING_CONTINUE").click();
	TestUtil.addCC();
	getObject("BILLING_CONTINUE").click();
	
	getObject("SUBMIT_ORDER").click();
	String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
	// Assert.assertTrue("Submit Order Failed",
	// confMsg.contains("Order Confirmation"));
	if (confMsg.contains("Order Confirmation")) {
		APPLICATION_LOG.debug("ExeComments --- Order Successful after Adding Credit Card");
	} else {
		APPLICATION_LOG.debug("ERROR --- Order Confirmation Check Failed after Adding Credit Card");
//		fail("Order Confirmation Check Failed - Order Confirm issue");
	}
	String orderConfirmNum = getObject("ORDER_CONFIRM_NUMBER").getText();
	APPLICATION_LOG.debug("ExeComments --- Order Confirmation Number after Adding Credit Card: " + orderConfirmNum);
	
	getObject("SIGN_OUT").click();
	}
	
	
	@Test(priority=2, dependsOnMethods={"addCreditCardInfoTest"}, dataProviderClass=TestDataProvider.class, dataProvider="MiscDataProvider")
	public void editCreditCardInfoTest(Hashtable<String, String> table) throws InterruptedException, IOException {
		
		validateRunmodes("EditCreditCardInfoTest", Constants.MISC_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		TestUtil.searchItem("diary", "Books");
		getObject("Age9-12").click();
		
		String expBCH1 = getObject("BREADCRUMB_H1").getText();
		String expBCH2 = getObject("BREADCRUMB_H2").getText();
		String expBCH3 = getObject("BREADCRUMB_H3").getText();
		
		
		if (expBCH1.contains("diary") && expBCH2.contains("Books") && expBCH3.contains("Age 9 - 12")){
			TestUtil.selectBooks(1, false);
		}else{
			APPLICATION_LOG.debug("Filtered items not matching with Breadcrumb values");
		}
		
		getObject("CHECKOUT_NOW").click();
		
		getObject("INLINE_EMAIL").sendKeys("testuser2@gmail.com");
		getObject("INLINE_PWD").sendKeys("testuser2");
		getObject("INLINE_SBTN").click();
		getObject("DELETE_ONLYADD").click();
		
		TestUtil.shippingAddress("557 Broadway", "New York", "NY - New York", "10012");
		getObject("SHIPPING_CONTINUE").click();
		
		getObject("EDIT_CCARD").click();
		
		TestUtil.popUpHandler();
		
		Select sel = new Select(getObject("CARD_EXP_YEAR"));
		sel.selectByVisibleText("2018");
		getObject("EDIT_CARD_SCODE").sendKeys("123");
		Select sel3 = new Select(getObject("SALUTATION"));
//		Thread.sleep(2000);
		sel3.selectByVisibleText("MR");
		getObject("EDIT_CARD_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		
		getObject("SUBMIT_ORDER").click();
		String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
		// Assert.assertTrue("Submit Order Failed",
		// confMsg.contains("Order Confirmation"));
		if (confMsg.contains("Order Confirmation")) {
			APPLICATION_LOG.debug("ExeComments --- Order Successful after editting the existing Credit Card information");
		} else {
			APPLICATION_LOG.debug("ERROR --- Order Confirmation Check FAILED after editting the existing Credit Card information");
			Assert.fail("Order Confirmation Check Failed - Order Confirm issue");
		}
		String orderConfirmNum = getObject("ORDER_CONFIRM_NUMBER").getText();
		APPLICATION_LOG.debug("ExeComments --- Order Confirmation Number after editting the existing Credit Card information: " + orderConfirmNum);
		
		getObject("SIGN_OUT").click();
		
  }
	
	@Test(priority=3, dependsOnMethods={"addCreditCardInfoTest"}, dataProviderClass=TestDataProvider.class, dataProvider="MiscDataProvider")
	public void deleteCreditCardInfoTest(Hashtable<String, String> table) throws InterruptedException, IOException {
		
		validateRunmodes("DeleteCreditCardInfoTest", Constants.MISC_SUITE, table.get(Constants.RUNMODE_COL));
		
		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		getObject("SHOP_BY_GENERE").click();
		getObject("HISTORY").click();
		Thread.sleep(3000);
		getObject("BOOKS").click();
		
		String expBCH1 = getObject("BREADCRUMB_H1").getText();
		String expBCH2 = getObject("BREADCRUMB_H2").getText();
		
		if (expBCH1.contains("History") && expBCH2.contains("Books")){
			TestUtil.selectBooks(1, false);
		}else{
			APPLICATION_LOG.debug("Error --- Filtered items not matching with Breadcrumb values");
		}
		
		getObject("CHECKOUT_NOW").click();
		
		TestUtil.shippingAddress("557 Broadway", "New York", "NY - New York", "10012");
		getObject("SHIPPING_CONTINUE").click();
		
		getObject("BILLING_SIGNIN").click();
		
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
		WebElement loginBox = driver.findElement(By.xpath("//*[@id='loginId']"));
		loginBox.sendKeys("testuser2@gmail.com");
		WebElement pwdBox = driver.findElement(By.xpath("//*[@id='password']"));
		pwdBox.sendKeys("testuser2");
		driver.findElement(By.xpath("//*[@id='nextbutton']")).click();
		
		Thread.sleep(6000);
		getObject("DELETE_CARD").click();
//driver.findElement(By.className("delete-wallet-creditcard-link")).click();
//		driver.findElement(By.cssSelector("a.delete-wallet-creditcard-link")).click();
		WebElement cardCount = getObject("CARD_COUNT");
		
		if (cardCount == null){
			APPLICATION_LOG.debug("ExeComments --- No Card Information available");
		}else{
			APPLICATION_LOG.debug("Error --- Hmmm... Card not Deleted");
		}
		
//		driver.findElement(By.cssSelector("a.edit-link edit-ShppingAddressModule-link")).click();
		getObject("SHIPPING_EDIT").click();
//		getObject("SHIPPING_CONTINUE").click();
		driver.findElement(By.cssSelector("a.delete-shipping-address-link")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("a.delete-shipping-address-link")).click();
		
		getObject("HOME_IMAGE").click();
		
		TestUtil.clearCart();
		
		getObject("SIGN_OUT").click();
		
	}

	private void fail(String string) {
		// TODO Auto-generated method stub
		
	}
	
//	@AfterMethod
//	public static void takeScreenShotOnFailure(ITestResult testResult)throws IOException {
//		if (testResult.getStatus() == ITestResult.FAILURE) {
//			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//			System.out.println(testResult.getName());
//			String screenShotFileName = CONFIG.getProperty("SCREENSHOT_DIR") + "/" + testResult.getName() + ".jpg";
//			FileUtils.copyFile(scrFile, new File(screenShotFileName));
//		}
//	}
	
	@AfterMethod
	public void shutDriver() throws InterruptedException {

		try {
			dispose();
		} catch (Throwable t) {
			return;
		}
	}
	
}

