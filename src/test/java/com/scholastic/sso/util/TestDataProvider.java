package com.scholastic.sso.util;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.scholastic.sso.TestBase;

public class TestDataProvider {
	
	@DataProvider(name="PlaceOrdersDataProvider")
		public static Object[][] getDataPlaceOrders(Method m) throws IOException{
		TestBase.initialize();
//		Xls_Reader xls1 = new Xls_Reader("C:\\Users\\chaitman\\Desktop\\SSO\\SSOFwk\\SuiteA.xlsx");
//		System.out.println(TestBase.prop.getProperty("xlsFileLocation"));
		Xls_Reader xls1 = new Xls_Reader(TestBase.CONFIG.getProperty("xlsFileLocation")+Constants.PLACEORDERS_SUITE+".xlsx");
		
//		return Utility.getData("test1", xls1);
		return TestUtil.getData(m.getName(), xls1);
		}
	
	@DataProvider(name="ShippingValidationDataProvider")
	public static Object[][] getDataShippingValidation(Method m) throws IOException{
		TestBase.initialize();
	Xls_Reader xls1 = new Xls_Reader(TestBase.CONFIG.getProperty("xlsFileLocation")+Constants.SHIPPINGVALIDATION_SUITE+".xlsx");
	
	return TestUtil.getData(m.getName(), xls1);
	}
	
	@DataProvider(name="MiscDataProvider")
	public static Object[][] getDataMisc(Method m) throws IOException{
		TestBase.initialize();
	Xls_Reader xls1 = new Xls_Reader(TestBase.CONFIG.getProperty("xlsFileLocation")+Constants.MISC_SUITE+".xlsx");
	
	return TestUtil.getData(m.getName(), xls1);
	}

}
