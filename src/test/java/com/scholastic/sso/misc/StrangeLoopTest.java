package com.scholastic.sso.misc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scholastic.sso.TestBase;
import com.scholastic.sso.util.TestUtil;

public class StrangeLoopTest extends TestBase{
	
	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException {
		initialize();
	}
	
  @Test
  public void hoverAndClickMenuTabs() throws IOException, InterruptedException {
	  TestUtil.goToStore(CONFIG.getProperty("STAGE_URL"));
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

}
