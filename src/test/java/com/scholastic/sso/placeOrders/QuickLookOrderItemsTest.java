package com.scholastic.sso.placeOrders;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

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

public class QuickLookOrderItemsTest extends TestBase {
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}
	// @Test(dataProvider="getData")
	// @Test(dataProviderClass=TestDataProvider.class,
	// dataProvider="suiteADataProvider")
	@Test(dataProviderClass = TestDataProvider.class, dataProvider = "PlaceOrdersDataProvider")
	public void quickLookOrderItemsTest(Hashtable<String, String> table) throws IOException,
			InterruptedException {

		APPLICATION_LOG.debug("******** Executing -  Quick Look Order Items Test ********");

		validateRunmodes("QuickLookOrderItemsTest", Constants.PLACEORDERS_SUITE,table.get(Constants.RUNMODE_COL));

		TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
		TestUtil.searchItem("diary", "Books");
		TestUtil.quickLook();
		if (!TestUtil.isEligibleForFreeShipping()) {
			getObject("TOYS_GAMES_NAV").click();
			TestUtil.addItemsToCart();
		}
		getObject("CHECKOUT_NOW").click();

		// Check for Standard_Shipping
		if (driver.findElement(By.id("Standard_Shipping")).isSelected()) {
			APPLICATION_LOG.debug("ExeComments --- Appropriate Shipping method selected");
		} else {
			APPLICATION_LOG.debug("ExeComments --- Incorrect Shippign method selected");
			fail("Incorrect Shippign method selected");
		}
  
		TestUtil.LoginTest();
		Thread.sleep(3000);
		getObject("SHIPPING_CONTINUE").click();
		Thread.sleep(3000);
		getObject("BILLING_CONTINUE").click();

		String mainWindowHandle1 = driver.getWindowHandle();
		System.out.println(mainWindowHandle1);
		Thread.sleep(3000);
		String orderAmtBeforeEdit = getObject("ORDER_AMOUNT").getText();
		APPLICATION_LOG.debug("ExeComments --- Order Amount when 2 items in the Cart: "+ orderAmtBeforeEdit);

		getObject("EDIT_CART").click();
		Set s = driver.getWindowHandles();
		Iterator ite = s.iterator();
		while (ite.hasNext()) {
			java.lang.String popupHandle = ite.next().toString();
			if (!popupHandle.contains(mainWindowHandle1)) {
				driver.switchTo().window(popupHandle);
			}
		}
		getObject("REMOVE_ITEM").click();
		getObject("OVERLAY_CONTINUE").click();
		String mainWindowHandle2 = driver.getWindowHandle();
		getObject("SHIPPING_EDIT").click();
		// getObject("TWO_DAY_SHIPPING").click();
		Thread.sleep(3000);
		WebElement radioButton = driver.findElement(By.id("Standard_Shipping"));
		radioButton.click();
		getObject("SHIPPING_CONTINUE").click();
		getObject("BILLING_CONTINUE").click();
		Thread.sleep(3000);
		String orderAmtAfterEdit = getObject("ORDER_AMOUNT").getText();
		APPLICATION_LOG.debug("ExeComments --- Order Amount when 1 item removed from the Cart: "+ orderAmtAfterEdit);

		if (orderAmtBeforeEdit.equals(orderAmtAfterEdit)) {
			APPLICATION_LOG.debug("Order total has no effect after removing item");
		} else {
			Thread.sleep(3000);
			getObject("SUBMIT_ORDER").click();
		}

		String confMsg = getObject("ORDER_CONFIRM_MSG").getText();
		if (confMsg.contains("Order Confirmation")) {
			APPLICATION_LOG.debug("ExeComments --- Order Successful");
		} else {
			APPLICATION_LOG.debug("ERROR --- Order Confirmation Check Failed");
			fail("Order Confirmation Check Failed");
		}

		getObject("SIGN_OUT").click();

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
