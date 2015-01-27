package samples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadingProps {
	public static void main(String[] args) throws IOException {
		
//		System.out.println(System.getProperty("user.dir"));
//		String path = "C:\\Users\\chaitman\\Desktop\\SeleniumWorkspace\\Scholastic_SSO\\src\\test\\resources\\project.properties";
		String path = System.getProperty("user.dir")+"\\src\\test\\resources\\project.properties";
		
		Properties prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(path);
			prop.load(fs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(prop.getProperty("fileLocation"));
	}

}
