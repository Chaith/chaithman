package com.scholastic.sso.misc;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.BeforeTest;

import com.scholastic.sso.TestBase;

public class NewTest extends TestBase {
  @BeforeTest
  public void beforeTest() {
	  createResultsFolder();
  }
	public static void createResultsFolder() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HHmmss");
		String resultsDirectory = CONFIG.getProperty("RESULTS_DIR") + "/" + dateFormat.format(new Date());
		String screenShotsDir = resultsDirectory + "/screenshots";
		String logDir = resultsDirectory + "/logs";
		createResultsFolder(new File(resultsDirectory));
		createResultsFolder(new File(screenShotsDir));
		createResultsFolder(new File(logDir));
		OR.put("SCREENSHOT_DIR", screenShotsDir);
		OR.put("LOG_DIR", logDir);
		
	}

	public static void createResultsFolder(File dirName) {
		if (!dirName.exists()) {
			dirName.mkdir();
		}
	}

}
