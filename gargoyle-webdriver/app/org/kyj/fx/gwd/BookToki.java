/********************************
 *	프로젝트 : gargoyle-webdriver
 *	패키지   : org.kyj.fx.gwd
 *	작성일   : 2023. 2. 26.
 *	작성자   : (zaruous@naver.com)
 *******************************/
package org.kyj.fx.gwd;

import java.io.File;
import java.util.List;

import org.kyj.fx.gwd.util.DownloadUtil;
import org.kyj.fx.gwd.util.FxUtil;
import org.kyj.fx.gwd.util.WebDriverInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author (zaruous@naver.com)
 *
 */
public class BookToki {

	private WebDriver driver;

	public BookToki() {
		driver = WebDriverInstance.getInstance().getDriver();
	}

	void changeHandle() {
		WebDriverInstance.changeLastTabHandle();
	}

	/**
	 * @작성자 : (zaruous@naver.com)
	 * @작성일 : 2023. 2. 26.
	 */
	public void download(File outDir) {
		changeHandle();

		String cssSelector = WebDriverInstance.getConfig().getProperty("tokiBoyCssSelector", "p img");
		List<WebElement> findElements = driver.findElements(By.cssSelector(cssSelector));

		List<String> imgList = FxUtil.toImageSrcList(findElements);
		DownloadUtil.download(outDir, imgList);
	}

}
