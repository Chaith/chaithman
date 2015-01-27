package com.scholastic.sso.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;

import com.scholastic.sso.TestBase;


public class TestUtil extends TestBase{
	
	public static boolean isSuiteRunnable(String suiteName, Xls_Reader xls){
		int rows = xls.getRowCount(Constants.SUITE_SHEET);
		
		for(int rNum=2;rNum<=rNum;rNum++){
			String data = xls.getCellData(Constants.SUITE_SHEET, Constants.SUITENAME_COL, rNum);
//			System.out.println(data);
			if(data.equals(suiteName)){
				String runmode = xls.getCellData(Constants.SUITE_SHEET, Constants.RUNMODE_COL, rNum);
				if(runmode.equals(Constants.RUNMODE_YES))
					return true;
				else
					return false;
			}
		}
		
		
		return false;
		
	}
	
	public static boolean isTestCaseRunnable(String testCase, Xls_Reader xls){
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);
		
		for(int rNum=2;rNum<=rows;rNum++){
			String testNameXls = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TESTCASES_COL, rNum);
			if(testNameXls.equalsIgnoreCase(testCase)){
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);
				if(runmode.equalsIgnoreCase(Constants.RUNMODE_YES))
					return true;
				else
					return false;
			}
		}
		return false;
		
	}
	
	
	public static Object[][] getData(String testName, Xls_Reader xls){
		int rows = xls.getRowCount(Constants.DATA_SHEET);
//		APPLICATION_LOG.debug("Total number of Rows - "+ rows);
		
		//Row number for test cases
		int testCaseRowNum=1;
		for(testCaseRowNum=1;testCaseRowNum<=rows;testCaseRowNum++){
			String testNameXls = xls.getCellData(Constants.DATA_SHEET, 0, testCaseRowNum);
			if(testNameXls.equalsIgnoreCase(testName))
			break;
		}
//		APPLICATION_LOG.debug("Test starts from row Number - " + testCaseRowNum);
		
		int dataStartRowNum = testCaseRowNum+2;
		int colStartRowNum = testCaseRowNum+1;
		
		//Rows of data
		int testRows = 1;
		while(!xls.getCellData(Constants.DATA_SHEET, 0, dataStartRowNum+testRows).equals("")){
			testRows++;
		}
//		APPLICATION_LOG.debug("Total rows of data are - " + testRows);
		
		//Columns of data
		int testCols=0;
		while(!xls.getCellData(Constants.DATA_SHEET, testCols, colStartRowNum).equals("")){
			testCols++;
		}
//		testCols++;
//		Object[][] data = new Object[testRows][testCols];
		Object[][] data = new Object[testRows][1];
//		APPLICATION_LOG.debug("Total columns are - " + testCols);
		
		int r=0;
		for(int rNum=dataStartRowNum;rNum<(dataStartRowNum+testRows);rNum++){
			Hashtable<String, String> table = new Hashtable<String, String>();
			for(int cNum=0;cNum<testCols;cNum++){
				//System.out.println(xls.getCellData(Constants.DATA_SHEET, cNum, rNum));
//				data[r][cNum] = xls.getCellData(Constants.DATA_SHEET, cNum, rNum);
				table.put(xls.getCellData(Constants.DATA_SHEET, cNum, colStartRowNum), xls.getCellData(Constants.DATA_SHEET, cNum, rNum));
			}
			data[r][0] = table;
			r++;
		}
		
		//0,0 0,1 i.e. r value for 1st row
		//1,0 1,1 i.e. r value for 2nd row
		return data;
		

	}
	
	/****************************************************/
	/****************************************************/
	/*********** COPIED TEST UTIL FOLLOWS ***************/
	/****************************************************/
	/**
	 * @throws InterruptedException **************************************************/
	
	public static double checkFreeShipping() throws InterruptedException {
		getObject("PREKG_IMG_ON_HOMEPAGE").click();
		String title = driver.getTitle();
		APPLICATION_LOG.debug("Title captured - "+title);
		String homePageTitle = "Books - Grade PreK - The Scholastic Store - The Scholastic Store";
			
		if (!title.equals(homePageTitle)) {
			fail ("Not on Home page --- Title check Failed");
			APPLICATION_LOG.debug("ERROR --- Not on Home page");
		}
		
		WebElement firstCatridge = getObject("PREKG_PAGE_FIRST_CARTRIDGE");
		Thread.sleep(1000);
		firstCatridge.findElement(By.linkText("See All")).click();
		Thread.sleep(3000);
		WebElement results = getObject("RESULTS_CONTAINER");
		Thread.sleep(3000);
		List<WebElement> iCount = results.findElements(By.tagName("img"));
		Random r = new Random();
		iCount.get(r.nextInt(iCount.size())).click();
		APPLICATION_LOG.debug("ExeComments --- Title added: " + driver.getTitle());
		
		if (getObject("ADD_TO_CART").isDisplayed()) {
			getObject("ADD_TO_CART").click();
		} else {
			getObject("HOME_IMAGE").click();
			checkFreeShipping();
		}
		
		String actTitle = driver.getTitle();
		String expTitle = "Scholastic Store Online";

		if (!actTitle.equals(expTitle)) {
			fail ("Not taken to the Add To cart page --- Title check Failed");
		}
			
		String dollars = getObject("INITIAL_PRICE").getText();
		Double actPrice = Double.parseDouble(dollars.substring(1, dollars.length()));
		Double fsEliPrice = 35.00;
		
		if (actPrice < fsEliPrice) {
			getObject("HOME_IMAGE").click();
			actPrice = checkFreeShipping();
		} else {
			APPLICATION_LOG.debug("ExeComments --- Cost of Books are now: " + actPrice);
		}
		return actPrice;
	}	
	
	private static void fail(String string) {
		
	}

	public static void hoverClickRandomResult() throws InterruptedException {
		WebElement results = getObject("RESULTS_CONTAINER");
		List<WebElement> iCount = results.findElements(By.tagName("img"));
		Random r = new Random();
		WebElement randomItem = iCount.get(r.nextInt(iCount.size()));
		System.out.println(randomItem.getAttribute("alt"));
		Actions act = new Actions(driver);
		act.moveToElement(randomItem).build().perform();
//		WebElement quickLook = driver.findElement(By.className("QuickLookButton"));
//		quickLook.click();
//		WebElement quickLook = 
				act.moveToElement(randomItem.findElement(By.xpath("//input[@class, 'QuickLookButton' and contains(@alt, 'Quick Look')]"))).click().build().perform();
	}
	
	public static boolean isEligibleForFreeShipping() {
		String dollarPrice = getObject("INITIAL_PRICE").getText();
		Double actPrice = Double.parseDouble(dollarPrice.substring(1, dollarPrice.length()));
		if (actPrice < Double.parseDouble(CONFIG.getProperty("FREE_SHIPPING_THRESHOLD_PRICE"))) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void registerUser(){
		getObject("EMAIL_ADDRESS").sendKeys("Chaith123456@gmail.com");
		getObject("CONFIRM_EMAIL").sendKeys("Chaith123456@gmail.com");
		getObject("PASSWORD").sendKeys("Pwd123");
		getObject("CONFIRM_PASSWORD").sendKeys("Pwd123");
		getObject("TERMS_AND_CONDITIONS").click();
		getObject("PRIVACY_POLICY").click();
		getObject("CREATE_ACCOUNT_BUTTON").click();
		
		String errRegistration = getObject("CONFIRM_REGISTRATION").getText();
		if (errRegistration.contains("Create an Account")){
			APPLICATION_LOG.debug("ExeComments --- User already exists - Try new Email ID");
		}else{
			APPLICATION_LOG.debug("ExeComments --- Registration Successful");
		}
	}

	public static void goToStore(String url) {
		driver.get(url);
		driver.manage().window().maximize();
	}
	
	public static void searchItem(String searchKey, String filterBy) throws InterruptedException {	
		getObject("SEARCH_BOX").sendKeys(searchKey);
		Select searchCombo = new Select(getObject("SEARCH_FILTER_COMBO"));
		Thread.sleep(2000);
		searchCombo.selectByVisibleText(filterBy);
		Thread.sleep(2000);
		getObject("SEARCH_SUBMIT_BUTTON").click();
		Thread.sleep(2000);
	}
	
	public static void topNavigation(String topNavMenu) {
		getObject(topNavMenu).click();
	}

	
	public static void quickLook() {
		WebElement Elem1 = driver.findElement(By.xpath("//*[@id='quickLookForm-138240']/div/table/tbody/tr/td/div/div/a/img"));
		WebElement Elem2 = driver.findElement(By.xpath("//*[@id='quickLookForm-138240']/div/input"));
		
		Actions act = new Actions(driver);
		act.moveToElement(Elem1).build().perform();
		String mainWindowHandle = driver.getWindowHandle();
		Elem2.click();
		
		Set s = driver.getWindowHandles();
		
		Iterator ite = s.iterator();
		while(ite.hasNext())
	    {
	         java.lang.String popupHandle=ite.next().toString();
	         if(!popupHandle.contains(mainWindowHandle))
	         {
	               driver.switchTo().window(popupHandle);
	         }
	    }
	    WebElement Elem3 = driver.findElement(By.xpath("//*[@id='inp-add-to-cart']"));
		Elem3.click();
//		WebElement Elem4 = driver.findElement(By.xpath("//*[@id='ShoppingCartForm']/div[5]/div/div[2]/input"));
//		Elem4.click();
	}
	
	public static void addItemsToCart() {
		WebElement Elem1 = driver.findElement(By.xpath("//*[@id='quickLookForm-155052']/div/table/tbody/tr/td"));
		WebElement Elem2 = driver.findElement(By.xpath("//*[@id='quickLookForm-155052']/div/input"));
		
		Actions act = new Actions(driver);
		act.moveToElement(Elem1).build().perform();
		String mainWindowHandle = driver.getWindowHandle();
		Elem2.click();
		
		Set s = driver.getWindowHandles();
		
		Iterator ite = s.iterator();
		while(ite.hasNext())
	    {
	         java.lang.String popupHandle=ite.next().toString();
	         if(!popupHandle.contains(mainWindowHandle))
	         {
	               driver.switchTo().window(popupHandle);
	         }
	    }
	    WebElement Elem3 = driver.findElement(By.xpath("//*[@id='inp-add-to-cart']"));
		Elem3.click();
	}

	public static void shippingAddress(String address1, String city, String state, String zip) {
		getObject("FRIST_NAME").sendKeys("Chaithanya");
		getObject("LAST_NAME").sendKeys("Mangarai");
		getObject("ADDRESS_1").sendKeys(address1);
		getObject("CITY").sendKeys(city);
		Select sel2 = new Select(getObject("SELECT_STATE"));
		sel2.selectByVisibleText(state);
		getObject("ZIP").sendKeys(zip);
		getObject("PHONE_1").sendKeys("798");
		getObject("PHONE_2").sendKeys("798");
		getObject("PHONE_3").sendKeys("7981");
		getObject("SHIPPING_INFO_LABEL").sendKeys("Work Address");
//		getObject("SHIPPING_CONTINUE").click();
	}
	
	public static void shippingAddressPOBox() {
		getObject("FRIST_NAME").sendKeys("Chaith");
		getObject("LAST_NAME").sendKeys("Man");
		getObject("ADDRESS_1").sendKeys("PO Box 1001");
		getObject("CITY").sendKeys("Falls Church");
		Select sel2 = new Select(getObject("SELECT_STATE"));
		sel2.selectByVisibleText("VA - Virginia");
		getObject("ZIP").sendKeys("22041");
		getObject("PHONE_1").sendKeys("798");
		getObject("PHONE_2").sendKeys("798");
		getObject("PHONE_3").sendKeys("7981");
		getObject("SHIPPING_INFO_LABEL").sendKeys("PO Box, Address");
		getObject("POBOX_CHECK").click();
//		getObject("SHIPPING_CONTINUE").click();
	}
	
	public static void billingAddress() throws InterruptedException {
		getObject("SAME_AS_SHIPPING").click();
		Select sel3 = new Select(getObject("SALUTATION"));
		Thread.sleep(2000);
		sel3.selectByVisibleText("MR");
		getObject("EMAIL_ID").sendKeys("cman@yahoo.com");
		getObject("CONFIRM_EMAIL_ID").sendKeys("cman@yahoo.com");
		Select sel6 = new Select(getObject("CARD_TYPE"));
		sel6.selectByVisibleText("VISA");
		getObject("CARD_NUMBER").sendKeys("4111111111111111");
		Select sel4 = new Select(getObject("CARD_EXP_MONTH"));
		sel4.selectByVisibleText("12");
		getObject("SECURITY_CODE").sendKeys("123");
		getObject("BILLING_CONTINUE").click();
	}
	
	public static void billingAddressNoEmail() throws InterruptedException {
		getObject("SAME_AS_SHIPPING").click();
		Select sel3 = new Select(getObject("SALUTATION"));
		Thread.sleep(2000);
		sel3.selectByVisibleText("MR");
//		getObject("EMAIL_ID").sendKeys("cman@yahoo.com");
//		getObject("CONFIRM_EMAIL_ID").sendKeys("cman@yahoo.com");
		Select sel6 = new Select(getObject("CARD_TYPE"));
		sel6.selectByVisibleText("VISA");
		getObject("CARD_NUMBER").sendKeys("4111111111111111");
		Select sel4 = new Select(getObject("CARD_EXP_MONTH"));
		sel4.selectByVisibleText("12");
		getObject("SECURITY_CODE").sendKeys("123");
		getObject("BILLING_CONTINUE").click();
	}
	
	public static void addCC() throws InterruptedException {
		getObject("SAME_AS_SHIPPING").click();
		Select sel3 = new Select(getObject("SALUTATION"));
		Thread.sleep(2000);
		sel3.selectByVisibleText("MR");
//		getObject("EMAIL_ID").sendKeys("cman@yahoo.com");
//		getObject("CONFIRM_EMAIL_ID").sendKeys("cman@yahoo.com");
		Select sel6 = new Select(getObject("CARD_TYPE"));
		sel6.selectByVisibleText("VISA");
		getObject("CARD_NUMBER").sendKeys("4111111111111111");
		Select sel4 = new Select(getObject("CARD_EXP_MONTH"));
		sel4.selectByVisibleText("12");
		getObject("SECURITY_CODE").sendKeys("123");
		getObject("BILLING_CONTINUE").click();
	}
	
	public static double RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
    return Double.valueOf(df2.format(val));
	}
	
	public static void LoginTest () {
		
		WebElement signInLink = driver.findElement(By.xpath("//*[@id='SPS_Container']/ul/li[1]/a"));
		signInLink.click();
		
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
		WebElement loginBox = driver.findElement(By.xpath("//*[@id='loginId']"));
		loginBox.sendKeys("cmangarai-consultant@scholastic.com");
		WebElement pwdBox = driver.findElement(By.xpath("//*[@id='password']"));
		pwdBox.sendKeys("M123");
		driver.findElement(By.xpath("//*[@id='nextbutton']")).click();
		}
	
public static void LoginTestSce5 (){
		
		WebElement signInLink = driver.findElement(By.xpath("//*[@id='SPS_Container']/ul/li[1]/a"));
		signInLink.click();
		
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
		WebElement loginBox = driver.findElement(By.xpath("//*[@id='loginId']"));
		loginBox.sendKeys("Chaith123456@gmail.com");
		WebElement pwdBox = driver.findElement(By.xpath("//*[@id='password']"));
		pwdBox.sendKeys("Pwd123");
		driver.findElement(By.xpath("//*[@id='nextbutton']")).click();
		}

public static void LoginTestSce10 (){
	
	WebElement signInLink = driver.findElement(By.xpath("//*[@id='SPS_Container']/ul/li[1]/a"));
	signInLink.click();
	
	driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
	WebElement loginBox = driver.findElement(By.xpath("//*[@id='loginId']"));
	loginBox.sendKeys("testuser1@gmail.com");
	WebElement pwdBox = driver.findElement(By.xpath("//*[@id='password']"));
	pwdBox.sendKeys("testuser1");
	driver.findElement(By.xpath("//*[@id='nextbutton']")).click();
	}

public static void LoginTestSce1 (){
	
	WebElement signInLink = driver.findElement(By.xpath("//*[@id='SPS_Container']/ul/li[1]/a"));
	signInLink.click();
	
	driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
	WebElement loginBox = driver.findElement(By.xpath("//*[@id='loginId']"));
	loginBox.sendKeys("Chaith1234@gmail.com");
	WebElement pwdBox = driver.findElement(By.xpath("//*[@id='password']"));
	pwdBox.sendKeys("M123");
	driver.findElement(By.xpath("//*[@id='nextbutton']")).click();
	}

public static void LoginCC (){
	
	WebElement signInLink = driver.findElement(By.xpath("//*[@id='SPS_Container']/ul/li[1]/a"));
	signInLink.click();
	
	driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
	WebElement loginBox = driver.findElement(By.xpath("//*[@id='loginId']"));
	loginBox.sendKeys("testuser2@gmail.com");
	WebElement pwdBox = driver.findElement(By.xpath("//*[@id='password']"));
	pwdBox.sendKeys("testuser2");
	driver.findElement(By.xpath("//*[@id='nextbutton']")).click();
	}
	
	public static void selectAddress(String state) {
		WebElement cartForm = driver.findElement(By.xpath("//*[@id='ShippingAddress-Module']/div[2]"));
		List<WebElement> cartItems = cartForm.findElements(By.className("select-address-container"));
		String province = "";
		for (int i=0; i<cartItems.size(); i++){
			province = cartItems.get(i).findElement(By.className("stateOrProvinceName")).getText();
			if(province.equals(state)) {
				cartItems.get(i).findElement(By.className("ShippingAddress-radio-button")).click();
				break;
			}
		}
	}
	
	public static double selectToys(int noOfToys) throws InterruptedException {
		double totalweight = 0.00;
		List<String> toysNames = new ArrayList<String>();
		for (int i = 0; i < noOfToys; i++) {
			
			getObject("TOYS_GAMES_NAV").click();
//			driver.get("http://stores2qa2.scholastic.com/shop/Toys-and-Games/4502~4518~26~4577");
			getObject("TOYS_SEE_ALL").click();
			getObject("TOYS_SEE_ALL_AGAIN").click();
//			driver.findElement(By.xpath("//*[@id='BottomLong']/div[1]/div/div/div[3]/a")).click();
//			driver.findElement(By.xpath("//*[@id='ToprightMedium']/div[2]/div/div/div[3]/a")).click();
			
			WebElement results = getObject("RESULTS_CONTAINER");
			List<WebElement> iCount = results.findElements(By.tagName("img"));
			
			Random r = new Random();
			iCount.get(r.nextInt(iCount.size())).click();
			
			String toyName = getObject("TOY_NAME").getText();
			if (getObject("ADD_TO_CART").isDisplayed() && !toysNames.contains(toyName)) {
				toysNames.add(toyName); //Changed 0924
				String weight = driver.findElement(By.className("li-product-spec-weight")).getText();
//				System.out.println(weight);
				String weightInLbs = weight.split(":")[1].trim();
				totalweight = totalweight + Double.parseDouble(weightInLbs);
//				APPLICATION_LOG.debug("Weight in Lbs: " + totalweight);
				getObject("ADD_TO_CART").click();
			} else {
				i--;
			}
		}
		return totalweight;
	}		
//~~~~~~~~~~~~~~~~~~~~~~~~
		
	
		public static void selectBooks(int noOfBooks, boolean isFreeShipping) throws InterruptedException {
			List<String> booksList = new ArrayList<String>();
			String selectedBook = "";
			for (int i = 0; i < noOfBooks; i++) {
				getObject("HOME_IMAGE").click();
				getObject("POPULAR_WITH_TEACHERS_SEE_ALL").click();
//				getObject("POPULAR_WITH_TEACHERS_SEE_ALL_DEL").click();
				if (isFreeShipping) {
					getObject("LIST_PRICE_HIGH_TO_LOW").click();
					selectedBook = selectBooksInPriceRange(18.00, 30.00, booksList);//remove 2 from the function
				} else {
					selectedBook = selectBooksInPriceRange(12.00, 16.00, booksList);//remove 2 from the function
				}
				booksList.add(selectedBook);
			}
		}
		
		
//~~~~~~~~~~~~~~~~~~~~~~~~
		public static double itemsTotalPrice() {
			double sumOfItems = 0.0;
			WebElement tab = driver.findElement(By.cssSelector("table[id='ItemsTable']"));
			List<WebElement> items = tab.findElements(By.cssSelector("td[class='quantity']"));
			System.out.println(items.size());
			String priceXPath = "";
			for(int i=1; i<=items.size(); i++){
				priceXPath = "//*[@id='ItemsTable']/tbody/tr[" + i + "]/td[5]/span";
				String price = getObjectByString(priceXPath).getText();
//				WebElement price = driver.findElement(By.cssSelector("td[class^='price']span"));
				String tempPrice = price.split("\n")[0];
				tempPrice = tempPrice.substring(1, tempPrice.length());
				sumOfItems = sumOfItems + Double.parseDouble(tempPrice);
		}
			return sumOfItems;
		}
		
	
	public static void selectBooksBelowFreeShipping(int noOfBooksToSelect) {
		double estimatedPrice = 0.00;
		for (int i = 0; i < noOfBooksToSelect; i++) {
			estimatedPrice+= selectRandomBook(estimatedPrice);
			if (i != noOfBooksToSelect -1)
				getObject("HOME_IMAGE").click();
		}
	}

	
	//@@@@@@@@@ Change here
	public static Double selectRandomBook(double estimatedPrice) {
		getObject("POPULAR_WITH_TEACHERS_SEE_ALL").click();
		getObject("LIST_PRICE_LOW_TO_HIGH").click();
		WebElement results = getObject("RESULTS_CONTAINER");
		Random r = new Random();
		int randomNo = r.nextInt(12);
		String randomDivXpath = "//*[@id='DropletsList']/div[" + randomNo
				+ "]/div";
		// System.out.println(randomDivXpath);
		WebElement randomDiv = results.findElement(By.xpath(randomDivXpath));
		String priceXpath = randomDivXpath + "/div[2]/p";
		String price = driver.findElement(By.xpath(priceXpath)).getText();
		Double bookPrice = extractPrice(price);
		estimatedPrice += bookPrice;
		if (estimatedPrice < Double.parseDouble(OR.getProperty("FSPRICE"))) {
			randomDiv.findElement(By.tagName("img")).click();
//			if (!getObject("ADD_TO_CART").isDisplayed()){   // Added 0922
//				getObject("HOME_IMAGE").click();   // Added 0922
//				selectBooksBelowFreeShipping(noOfBooksToSelect);   // Added 0922
//			}   // Added 0922
			getObject("ADD_TO_CART").click();
		} else {
			return 0.00;
		}
		return bookPrice;
	}
	
	public static double extractPrice(String price) {
		String priceInDollars = price.split(":")[1].trim();
		return Double.parseDouble(priceInDollars.substring(1, priceInDollars.length()));
	}
	public static void selectBookByDiv(WebElement results, int divNo) {
		String randomDivXpath = "//*[@id='DropletsList']/div[" + divNo + "]/div";
		WebElement randomDiv = results.findElement(By.xpath(randomDivXpath));
	}

	public static Map<Double, Integer> priceListOnPage() {
		String priceXpath = "";
		String price = "";
		Map<Double, Integer> bookPriceList = new HashMap<Double,Integer>();
		for (int i = 0; i < 12; i++) {
			priceXpath = "//*[@id='DropletsList']/div[" + i + "]/div/div[2]/p";
			price = driver.findElement(By.xpath(priceXpath)).getText();
			bookPriceList.put(Double.parseDouble(price.substring(1, price.length())), i);
		}
        Map<Double, Integer> sortedBookPrice = sortByKeys(bookPriceList);
		return sortedBookPrice;
	}
	
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByKeys(Map<K,V> map){
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
        for(K key: keys){
            sortedMap.put(key, map.get(key));
        }
        return sortedMap;
    }


	public static double selectBook(double totalPrice) {
		getObject("POPULAR_WITH_TEACHERS_SEE_ALL").click();
		WebElement results = getObject("RESULTS_CONTAINER");
		Random r = new Random();
		int randomNo = r.nextInt(12);
		String randomDivXpath = "//*[@id='DropletsList']/div[" + randomNo + "]/div";
		WebElement randomDiv = results.findElement(By.xpath(randomDivXpath));
		String priceXpath = randomDivXpath + "/div[2]/p";
		String price = driver.findElement(By.xpath(priceXpath)).getText();
		return Double.parseDouble(price.substring(1, price.length()));
	}
	
	public static int getBooksCount() throws InterruptedException{
		
		List<String> bookFormats = new ArrayList<String>();
		bookFormats.add("Storia eBooks");
		bookFormats.add("Hardcover");
		bookFormats.add("Paperback");
		bookFormats.add("Audio CD");
		bookFormats.add("Board Book");
		
		WebElement orderReviewBox = getObject("ORDER_REVIEW_BOX");
		List<WebElement> items = orderReviewBox.findElements(By.className("item"));
		
		int bookCount = 0;
		for (int i = 0; i < items.size(); i++) {
			String itmPath1 = "//*[@id='ItemsTable']/tbody/tr[";
			String itmPath2 = "]/td[2]/div/div[2]";
			Thread.sleep(3000);
			String format = driver.findElement(
					By.xpath(itmPath1 + (i + 1) + itmPath2)).getText();
//			APPLICATION_LOG.debug("Format from the Pop Up - Iteration " + (i + 1)+ " - " + format);
			Thread.sleep(3000);
			if (bookFormats.contains(format))
			{
				bookCount++;
			}
		}
		APPLICATION_LOG.debug("Books count form the fucntion: " + bookCount);
		return bookCount;
	}

//	/****
//	 * @param shippingItems
//	 * @param typeOfBooks
//	 * @return 0 Shipping cart does not contain books
//	 * @return 1 Shipping cart contains only books
//	 * @return 2 Shipping cart contains mix of books & toys
//	 */
//	public static int getShippingType(Set<String> shippingItems, List<String> typeOfBooks) {
//		if (typeOfBooks.containsAll(shippingItems)) {
//			return 1;
//		}
//		shippingItems.retainAll(typeOfBooks);
//		if (shippingItems.size() > 0) {
//			return 2;
//		} else {
//			return 0;
//		}
//	}

	// Standard Shipping Charges
	public static double calcStandardShippingCharges(String state, boolean ispoBox, int shippingItemsType, int noOfBooks, double totalWeight) {
		double perShipmentPrice = 0.00;
		double perItemPrice = 0.00;
		double totCharge = 0.00; 
		if (ispoBox || state.equals("AK") || state.equals("HI") || state.equals("AA")) {
			if (shippingItemsType == 1) { // Books only 
				perShipmentPrice = 5.99;
				perItemPrice = 0.99 * noOfBooks;
			} else if (shippingItemsType == 0) { // Toys & Others
				perShipmentPrice = 9.99;
				perItemPrice = 0.99 *  totalWeight;
			} else { // Toys & Books
				perShipmentPrice = 9.99;
				perItemPrice = 0.99 *  totalWeight + 0.99 * noOfBooks;
			}
		} else { //State falls under 48 states 
			if (shippingItemsType == 1) { // Books only 
				perShipmentPrice = 2.99;
				perItemPrice = 0.99 * noOfBooks;
			} else if (shippingItemsType == 0) { // Toys & Others
				perShipmentPrice = 3.99;
				perItemPrice = 0.99 *  totalWeight;
			} else { // Toys & Books
				perShipmentPrice = 3.99;
				perItemPrice = 0.99 *  totalWeight + 0.99 * noOfBooks;
			}
		}
		totCharge = perItemPrice + perShipmentPrice;
		DecimalFormat df = new DecimalFormat("#.##");
		Double totalShipCharge = Double.valueOf(df.format(totCharge));
//		APPLICATION_LOG.debug("Shipping charges from the Function: " + totalShipCharge);
		return totalShipCharge; 
	}
	
	// Two Day Shipping Charges
	public static double calcTwoDayShippingCharges(String state, boolean ispoBox, int shippingItemsType, int noOfBooks, double totalWeight) {
		double perShipmentPrice = 0.00;
		double perItemPrice = 0.00;
		double totCharge = 0.00; 
		if (ispoBox || state.equals("AK") || state.equals("HI") || state.equals("AA")) {
			if (shippingItemsType == 1) { // Books only 
				perShipmentPrice = 28.99;
				perItemPrice = 1.99 * noOfBooks;
			} else if (shippingItemsType == 0) { // Toys & Others
				perShipmentPrice = 32.99;
				perItemPrice = 0.99 *  totalWeight;
			} else { // Toys & Books
				perShipmentPrice = 32.99;
				perItemPrice = 1.99 *  totalWeight + 0.99 * noOfBooks;
			}
		} else { //State falls under 48 states 
			if (shippingItemsType == 1) { // Books only 
				perShipmentPrice = 10.99;
				perItemPrice = 1.99 * noOfBooks;
			} else if (shippingItemsType == 0) { // Toys & Others
				perShipmentPrice = 10.99;
				perItemPrice = 0.99 *  totalWeight;
			} else { // Toys & Books
				perShipmentPrice = 10.99;
				perItemPrice = 0.99 *  totalWeight + 1.99 * noOfBooks;
			}
		}
		totCharge = perItemPrice + perShipmentPrice;
		DecimalFormat df = new DecimalFormat("#.##");
		Double totalShipCharge = Double.valueOf(df.format(totCharge));
		APPLICATION_LOG.debug("Shipping charges from the Function: " + totalShipCharge);
		return totalShipCharge; 
	}
	
	// One Day Shipping Charges
	public static double calcOneDayShippingCharges(String state, boolean ispoBox, int shippingItemsType, int noOfBooks, double totalWeight) {
		double perShipmentPrice = 0.00;
		double perItemPrice = 0.00;
		double totCharge = 0.00; 
		if (ispoBox || state.equals("AK") || state.equals("HI") || state.equals("AA")) {
			if (shippingItemsType == 1) { // Books only 
				perShipmentPrice = 38.99;
				perItemPrice = 4.99 * noOfBooks;
			} else if (shippingItemsType == 0) { // Toys & Others
				perShipmentPrice = 42.99;
				perItemPrice = 1.99 *  totalWeight;
			} else { // Toys & Books
				perShipmentPrice = 42.99;
				perItemPrice = 4.99 *  totalWeight + 1.99 * noOfBooks;
			}
		} else { //State falls under 48 states 
			if (shippingItemsType == 1) { // Books only 
				perShipmentPrice = 14.99;
				perItemPrice = 4.99 * noOfBooks;
			} else if (shippingItemsType == 0) { // Toys & Others
				perShipmentPrice = 18.99;
				perItemPrice = 18.99 *  totalWeight;
			} else { // Toys & Books
				perShipmentPrice = 18.99;
				perItemPrice = 1.99 *  totalWeight + 4.99 * noOfBooks;
			}
		}
		totCharge = perItemPrice + perShipmentPrice;
		DecimalFormat df = new DecimalFormat("#.##");
		Double totalShipCharge = Double.valueOf(df.format(totCharge));
		APPLICATION_LOG.debug("Shipping charges from the Function: " + totalShipCharge);
		return totalShipCharge; 
		
	}
	
	public static void popUpHandler() {
		String mainWindowHandle = driver.getWindowHandle();
		Set s = driver.getWindowHandles();
		Iterator ite = s.iterator();
		while (ite.hasNext()) {
			java.lang.String popupHandle = ite.next().toString();
			if (!popupHandle.contains(mainWindowHandle)) {
				driver.switchTo().window(popupHandle);
			}
		}
	}
	
	public static double getShippingCharges() throws InterruptedException {
//		String shipCharge = getObject("SHIP_CHARGES").getText();
		Thread.sleep(5000);
		String shipCharge = driver.findElement(By.xpath("//*[@id='OrderTotalBox']/div[2]/table/tbody/tr[2]/td[2]")).getText();
//		APPLICATION_LOG.debug("Shipping charges from application are: " + shipCharge);
		return Double.parseDouble(shipCharge.substring(1, shipCharge.length()));
//		*[@id='OrderTotalBox']/div[2]/table/tbody/tr[2]/td[2]/span
	}
	
	public static double getShippingChargesFromMiniCart() throws InterruptedException {
//		String shipCharge = getObject("SHIP_CHARGES").getText();
		Thread.sleep(5000);
		String shipCharge = driver.findElement(By.xpath("//*[@id='MiniCart-Module']/div[2]/table/tbody/tr[2]/td[2]/span")).getText();
		APPLICATION_LOG.debug("Shipping charges from application are: " + shipCharge);
		return Double.parseDouble(shipCharge.substring(1, shipCharge.length()));
//		*[@id='OrderTotalBox']/div[2]/table/tbody/tr[2]/td[2]/span
	}
	
	public static int popupItems() {
		WebElement editCartPP = getObject("ITEMS_TABLE");
		List<WebElement> editCartItems = editCartPP.findElements(By.tagName("img"));
		int itemsCountFromPopup = editCartItems.size();
		return itemsCountFromPopup;
	}
	
	public static void selAddress(String selState) throws InterruptedException {
		Thread.sleep(3000);
//		WebElement addContainer = driver.findElement(By.xpath("//*[@id='ShippingAddress-Module']/div[2]"));
		WebElement addContainer = driver.findElement(By.id("SubmitSelectShippingAddressForm"));
		List<WebElement> add = addContainer.findElements(By.className("select-address-container"));
		
		for(int i = 0; i < add.size(); i++){
			
			String part1 = "//*[@id='SubmitSelectShippingAddressForm']/div[";
			String part2 = "]/div[2]/table/tbody/tr/td[2]/label/span";
			
			String state = driver.findElement(By.xpath(part1 + (i + 1) + part2)).getText();
			if (state.contains(selState)){
				add.get(i).findElement(By.className("ShippingAddress-radio-button")).click();
			}
		}
	}
	
//################################################################################################################
//############To Delete Later - Test Functions##################################################################
//################################################################################################################
	
	public static void selectBooksBelowFreeShipping1() {
		
		getObject("POPULAR_WITH_TEACHERS_SEE_ALL").click();
//		getObject("LIST_PRICE_LOW_TO_HIGH").click();
		
		driver.findElement(By.xpath("//*[@id='quickLookForm-137870']/div/table/tbody/tr/td/div/div/a/img")).click();
		driver.findElement(By.xpath("//*[@id='addToCart']")).click();
		getObject("HOME_IMAGE").click();
		getObject("POPULAR_WITH_TEACHERS_SEE_ALL").click();
//		getObject("LIST_PRICE_LOW_TO_HIGH").click();
		driver.findElement(By.xpath("//*[@id='quickLookForm-164824']/div/table/tbody/tr/td/div/div/a/img")).click();
		driver.findElement(By.xpath("//*[@id='addToCart']")).click();
	}
	
	
	
	public static String selectBooksInPriceRange(double lowerPrice,double upperPrice, List<String> booksList) {
		WebElement results = getObject("RESULTS_CONTAINER");
		List<WebElement> items = driver.findElements(By.cssSelector("div[class^='ASOProductItemDisplay']"));
//		System.out.println(items.size());
		String bookName = "";
		boolean isBookSelected = false;
		boolean isOutOfStockItem = false;
		
		for (int i = 0; i < items.size(); i++) {
			WebElement priceObj = getObjectByCSS(items.get(i), "p[class*='format']");
			if (priceObj == null) continue;
			String price = priceObj.getText();
			bookName = items.get(i).findElement(By.cssSelector("h2[class*='productName']")).getText();
			Double bookPrice = extractPrice(price);
//			APPLICATION_LOG.debug("Book price: " + bookPrice);
			if (bookPrice >= lowerPrice && bookPrice <= upperPrice && !booksList.contains(bookName)) {
				booksList.add(bookName); // Uncomment this later - Out oF Stock relate issue caused this line to comment
//				APPLICATION_LOG.debug("Selected Book Title: " + bookName);
				items.get(i).findElement(By.tagName("img")).click();
				if (getObject("ADD_TO_CART").isDisplayed()) { 
//					getObject("ADD_TO_CART") != null
//      getObject("ADD_TO_CART").isDisplayed() && !toysNames.contains(toyName)
					getObject("ADD_TO_CART").click();
					isBookSelected = true;
					break;
				} else {
					isOutOfStockItem = true;
				}
			}
		}

		if (!isBookSelected) {
			if (getObject("SEARCH_NEXT_PAGE2") != null) {
				getObject("SEARCH_NEXT_PAGE2").click();
			} else if (isOutOfStockItem) {
				driver.navigate().back();
			} else {
				getObject("SEARCH_NEXT_PAGE").click();
			}
			selectBooksInPriceRange(lowerPrice, upperPrice, booksList);
		}
		return bookName;
	}

	public static void add2BooksBelowFSValue(){
		
		driver.get("http://stores2qa2.scholastic.com/SearchCmd?Ntt=Favorites&storeId=11301&catalogId=16551&langId=-11301&N=4502&allSearchSize=&ftorp=false&RecordSearchKey=&goToView=CategoryDisplayView&homeURL=custom&viewParam=CategoryDisplay&No=0&NewPageNumber=1");
		getObject("BOOK_ONE").click();
		getObject("ADD_TO_CART").click();
		driver.navigate().back();
		driver.navigate().back();
		getObject("BOOK_TWO").click();
		getObject("ADD_TO_CART").click();
		
	}
	
	public static void clearCart() throws InterruptedException {
//		driver.findElement(By.cssSelector("#MiniCart-ShoppingCartLink")).click();
		Thread.sleep(10000);
		getObjectByCSS("#MiniCart-ShoppingCartLink").click();
//		getObject("SHOPPING_CART").click();
		
		WebElement cartEmptyText = getObject("CART_EMPTY_TEXT");
//		Thread.sleep(15000);
		if(cartEmptyText!=null) {
			getObject("HOME_IMAGE").click();
		} else if(getObject("CART_TABLE").isDisplayed()){
		WebElement tab = driver.findElement(By.cssSelector("table[id='ItemsTable']"));
		List<WebElement> items = tab.findElements(By.cssSelector("td[class='quantity']"));
//		System.out.println(items.size());
		
		for(int i=0; i<items.size(); i++){
			WebElement remove = driver.findElement(By.cssSelector("a[class^='removeButton remove-item-link']"));
			remove.click();
			Thread.sleep(2000);
		}
	}
		
	}
	public static void signOut(){
		getObject("SIGN_OUT").click();
	}

//	public static void takeScreenShot(String fileName) {
//		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//		String screenShotFileName = CONFIG.getProperty("SCREENSHOT_DIR") + "/" + fileName.ge + ".jpg";
//		try {
//			FileUtils.copyFile(scrFile, new File(screenShotFileName));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static void takeScreenShotOnFailure(ITestResult testResult)throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			System.out.println(testResult.getName());
			String screenShotFileName = CONFIG.getProperty("SCREENSHOT_DIR") + "/" + testResult.getName() + ".jpg";
			FileUtils.copyFile(scrFile, new File(screenShotFileName));
		}
	}
	
	public static int getLinkCount(String menuItemXpath, String menuOptionsXpath) {
		  List<WebElement> sLinks;
		  WebElement mTab = getObject(menuItemXpath);
		  Actions builder = new Actions(driver);    
		  builder.moveToElement(mTab).build().perform();
		  WebElement mCont = getObject(menuOptionsXpath);
		  sLinks = mCont.findElements(By.tagName("a"));
		  return sLinks.size();
	}

	public static void clickMe(String menuItemXpath, String menuOptionsXpath, int linkNumber) {
		  List<WebElement> sLinks;
		  WebElement mTab = getObject(menuItemXpath);
		  Actions builder = new Actions(driver);    
		  builder.moveToElement(mTab).build().perform();
		  WebElement mCont = getObject(menuOptionsXpath);
		  sLinks = mCont.findElements(By.tagName("a"));
		  System.out.println("Link Number : " + linkNumber + "Link Name : " + sLinks.get(linkNumber).getText());
		  sLinks.get(linkNumber).click();
		  if (getObject("NORESULTSFOUND") != null) {
			  System.out.println("NO RESULTS found");
		  }
		  driver.navigate().back();
	}

}
