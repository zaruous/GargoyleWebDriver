/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd
 *	작성일   : 2023. 2. 25.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author (zaruous@naver.com)
 *
 */
public class WebDriverInstance {

	private static WebDriverInstance instance;
	private WebDriver createDriver;

	/**
	 * @throws FileNotFoundException
	 */
	private WebDriverInstance() throws FileNotFoundException {
		createDriver = createDriver(DriverType.Chrome);
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 25.
	 * @return
	 * @throws FileNotFoundException
	 */
	public static WebDriverInstance getInstance() {
		if (instance == null)
			try {
				instance = new WebDriverInstance();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		return instance;
	}

	private enum DriverType {
		Chrome, Exporer
	};

	private DriverType driverType = DriverType.Chrome;

	public DriverType getDriverType() {
		return driverType;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 25.
	 * @return
	 */
	public final WebDriver getDriver() {
		return createDriver;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 25.
	 * @param type
	 * @return
	 * @throws FileNotFoundException
	 */
	protected WebDriver createDriver(DriverType type) throws FileNotFoundException {
		WebDriver driver;

		getConfig().getProperty("driver.path", "");
		File driverFile = new File("lib/chrome/110", "chromedriver.exe");
		if (!driverFile.exists()) {
			System.err.println("chromedriver.exe does not eixsts.");
			throw new FileNotFoundException("Driver not found. " + driverFile);
		}

		System.setProperty("webdriver.chrome.driver", driverFile.getAbsolutePath());

		ChromeOptions options = new ChromeOptions();
		// options.addArguments("headless");
		// options.addArguments("--headless");

		driver = new ChromeDriver(options);

		return driver;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 25.
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Properties getConfig() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26. 
	 */
	public static void changeLastTabHandle() {
		WebDriver driver = getInstance().getDriver();
		Set<String> windowHandles = driver.getWindowHandles();
		ArrayList<String> tabs = new ArrayList<String>(windowHandles);
		driver.switchTo().window(tabs.get(tabs.size() - 1));
	}
}
