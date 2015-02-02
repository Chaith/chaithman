package com.scholastic.sso.misc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.TestDataProvider;
import com.scholastic.sso.util.TestUtil;

public class StrangeLoopTest extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}
	
//  @Test
  @Test(dataProviderClass=TestDataProvider.class, dataProvider="MiscDataProvider")
//	public void strangeLoopTest(Hashtable<String, String> table) throws InterruptedException, IOException{
  public void strangeLoopTest(Hashtable<String, String> table) throws IOException, InterruptedException {
	  validateRunmodes("StrangeLoopTest", Constants.MISC_SUITE, table.get(Constants.RUNMODE_COL));
	  APPLICATION_LOG.debug("******** Executing -  Strange Loop Test ********");
	  TestUtil.goToStore(CONFIG.getProperty("QA_URL"));
	  driver.findElement(By.xpath("//*[@id='StoreFooter']/div[4]/div[1]/a")).click();
	  Map<String, String> menuMap = new HashMap<String, String>();
	  menuMap.put("GIFTIDEASMENU", "GIFTIDEAS_MENUOPTIONS");
	  menuMap.put("BOOKSMENU", "BOOKS_MENUOPTIONS");
	  menuMap.put("TOYSANDGAMESMENU", "TOYSANDGAMES_MENUOPTIONS");
	  menuMap.put("EDUCATIONALMENU", "EDUCATIONAL_MENUOPTIONS");
	  
	  for (Entry<String, String> entry : menuMap.entrySet()) {
		  int subMenuItems = TestUtil.getLinkCount(entry.getKey(), entry.getValue());
		  for (int i = 0; i < subMenuItems; i++) {
			  if (entry.getKey().equals("TOYSANDGAMESMENU") && (i == 18 || i == 19)) {
				  continue;
			  }
//			  clickMe("TOYSANDGAMESMENU", "TOYSANDGAMES_MENUOPTIONS",i);
			  TestUtil.clickMe(entry.getKey(), entry.getValue(),i);
	  }
	}
	  
	  getObject("SALEMENU").click();
	  if (getObject("NORESULTSFOUND") != null) {
		  System.out.println("NO RESULTS found");
	  }
	  driver.navigate().back();
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
