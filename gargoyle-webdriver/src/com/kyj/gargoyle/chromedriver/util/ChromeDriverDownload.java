/********************************
 *	프로젝트 : Ani24-Downloader
 *	패키지   : com.kyj.gargoyle.chromedriver.util
 *	작성일   : 2019. 9. 30.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.gargoyle.chromedriver.util;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class ChromeDriverDownload {

	ChromeDriver driver;
	WebElement target;
	public void download() {
		Actions actions = new Actions(driver);
		actions.contextClick(target)
		.sendKeys(Keys.CONTROL, Keys.chord("S"))
		.sendKeys(Keys.ENTER)
		.build().perform();
		
	}
}
