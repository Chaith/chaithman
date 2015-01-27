package samples;

import com.scholastic.sso.util.Constants;
import com.scholastic.sso.util.Xls_Reader;

public class ReadingData {
	
//	@SuppressWarnings("unused")
	public static void main(String[] args){
		Xls_Reader xls = new Xls_Reader("C:\\Users\\chaitman\\Desktop\\SSO\\SSOFwk\\SuiteA.xlsx");
		String testName = "test1";
		int rows = xls.getRowCount(Constants.DATA_SHEET);
		System.out.println("Total number of Rows - "+ rows);
		
		//Row number for test cases
		int testCaseRowNum=1;
		for(testCaseRowNum=1;testCaseRowNum<=rows;testCaseRowNum++){
			String testNameXls = xls.getCellData(Constants.DATA_SHEET, 0, testCaseRowNum);
			if(testNameXls.equalsIgnoreCase(testName))
			break;
		}
		System.out.println("Test starts from row Number - " + testCaseRowNum);
		
		int dataStartRowNum = testCaseRowNum+2;
		int colStartRowNum = testCaseRowNum+1;
		
		//Rows of data
		int testRows = 1;
		while(!xls.getCellData(Constants.DATA_SHEET, 0, dataStartRowNum+testRows).equals("")){
			testRows++;
		}
		System.out.println("Total rows of data are - " + testRows);
		
		//Columns of data
		int testCols=0;
		while(!xls.getCellData(Constants.DATA_SHEET, testCols, colStartRowNum).equals("")){
			testCols++;
		}
//		testCols++;
		System.out.println("Total columns are - " + testCols);
		
		for(int rNum=dataStartRowNum;rNum<(dataStartRowNum+testRows);rNum++){
			for(int cNum=0;cNum<testCols;cNum++){
				System.out.println(xls.getCellData(Constants.DATA_SHEET, cNum, rNum));
			}
		}
		
	}

}
